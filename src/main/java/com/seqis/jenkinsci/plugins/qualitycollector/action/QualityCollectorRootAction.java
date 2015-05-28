package com.seqis.jenkinsci.plugins.qualitycollector.action;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Describable;
import hudson.model.RootAction;
import hudson.model.Actionable;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;
import jenkins.model.ModelObjectWithContextMenu;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import com.seqis.data.Datastore;
import com.seqis.data.entity.TestCaseExecution;
import com.seqis.data.entity.TestObjectVersion;
import com.seqis.data.ormlite.ORMLiteDatastore;
import com.seqis.jenkinsci.plugins.qualitycollector.Constants;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.MainPageDisplayDataDto;
import com.seqis.jenkinsci.plugins.qualitycollector.service.DisplayDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestExecutionSummaryService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

@Extension
public class QualityCollectorRootAction extends Actionable implements
		RootAction, ModelObjectWithContextMenu,
		Describable<QualityCollectorRootAction>, ActionWithDescription {
	
	private static final String QUALITY_COLLECTOR_DASHBOARD_URL = "quality_collector_dashboard";
	private static final String QUALITY_COLLECTOR_DASHBOARD_NAME = "Quality Collector Dashboard";
	private static final String QUALITY_COLLECTOR_DASHBOARD_ICONFILENAME = "/plugin/quality-collector/images/qc-symbol.png";
	private static final Logger logger = Logger.getLogger(Constants.QUALITY_COLLECTOR_LOGGER_NAME);
	
	private final DescriptorImpl descriptor;
	
	private Datastore datastore;
	private DisplayDataService displayDataService;
	private TableDataService tableDataService;
	private TestExecutionSummaryService testExecutionSummaryService;
	private TestObjectVersionService testObjectVersionService;

	@Extension
	public static final class DescriptorImpl extends Descriptor<QualityCollectorRootAction> {
		private String dbUrl;
		private String dbPassword;
		private String dbUser;
		private String dbDriverClass;
		private String defaultTestObjectVersionId;
		private QualityCollectorRootAction qualityCollectorRootAction;

		
		@DataBoundConstructor
		public DescriptorImpl(String dbUrl, String dbPassword, String dbUser,
				String dbDriverClass, String defaultTestObjectVersionId) {

			super.load();
			this.dbUrl = dbUrl;
			this.dbPassword = dbPassword;
			this.dbUser = dbUser;
			this.dbDriverClass = dbDriverClass;
			this.defaultTestObjectVersionId = defaultTestObjectVersionId;
			super.save();
		}

		@Override
		public String getDisplayName() {
			return this.getClass().getSimpleName();
		}

		public String getDbUrl() { return this.dbUrl; }
		public void setUrl(String dbUrl) {
			this.dbUrl = dbUrl;
			super.save();
		}

		public String getDbPassword() { return this.dbPassword; }
		public void setDbPassword(String dbPassword) {
			this.dbPassword = dbPassword;
			super.save();
		}

		public String getDbUser() { return this.dbUser; }
		public void setDbUser(String dbUser) {
			this.dbUser = dbUser;
			super.save();
		}

		public String getDbDriverClass() { return this.dbDriverClass; }
		public void setDbDriverClass(String dbDriverClass) {
			this.dbDriverClass = dbDriverClass;
			super.save();
		}

		public String getDefaultTestObjectVersionId() { return this.defaultTestObjectVersionId; }
		public void setDefaultTestObjectVersionId(String defaultTestObjectVersionId) {
			this.defaultTestObjectVersionId = defaultTestObjectVersionId;
			super.save();
		}

		public void setQualityCollectorRootAction(QualityCollectorRootAction qualityCollectorRootAction) { this.qualityCollectorRootAction = qualityCollectorRootAction; }

		@Override
		public boolean configure(StaplerRequest req, JSONObject config)
				throws hudson.model.Descriptor.FormException {
			
			final JSONObject qcConfig = config.getJSONObject("quality-collector");
			this.dbUrl = qcConfig.getString("dbUrl");
			this.dbPassword = qcConfig.getString("dbPassword");
			this.dbUser = qcConfig.getString("dbUser");
			this.dbDriverClass = qcConfig.getString("dbDriverClass");
			this.defaultTestObjectVersionId = qcConfig.getString("defaultTestObjectVersionId");
			//Datastore datastore;
			super.save();
			
			try {
				this.rebuildDatastore();
				//datastore = this.rebuildDatastore();
				//this.outerClass.initAllServices(datastore);
				//this.outerClass.replaceStandardActions();
				return super.configure(req, config);
			} catch (Throwable t) {
				//this.outerClass.initAllServices(null);
				throw new hudson.model.Descriptor.FormException(
						"DB connection not valid, Quality Collector will find no data",
						t,
						"DB connection not valid, Quality Collector will find no data");
			}
		}

		public FormValidation doTestConnection(
				@QueryParameter("dbUrl") final String dbUrl,
				@QueryParameter("dbUser") final String dbUser,
				@QueryParameter("dbPassword") final String dbPassword,
				@QueryParameter("dbDriverClass") final String dbDriverClass)
				throws IOException, ServletException {
			
			this.dbUrl = dbUrl;
			this.dbUser = dbUser;
			this.dbPassword = dbPassword;
			this.dbDriverClass = dbDriverClass;

			try {
				this.rebuildDatastore();
				return FormValidation.ok("Success");
			} catch (ExceptionInInitializerError e) {
				//StringWriter sw = new StringWriter();
				//PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace();
				if (e.getException() instanceof SQLException) { //TODO
					return FormValidation.error("DB connection not successful: " + e.toString());
				}
				return FormValidation.error("Fatal Server Exception: " + e.toString());
			} catch (Throwable t) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				t.printStackTrace(pw);				
				return FormValidation.error("Fatal Server Throwable: " + t.toString());
			}
		}

		public ListBoxModel doFillDefaultTestObjectVersionIdItems() {
			ListBoxModel items = new ListBoxModel();
			if (this.qualityCollectorRootAction.datastore != null) {
				for (TestObjectVersion actVersion : this.qualityCollectorRootAction.datastore.getAllTestObjectVersions()) {
					items.add(actVersion.getName(), Long.toString(actVersion.getId()));
				}
			}
			return items;
		}

		public void setSystemParamsForDB() {
			if (this.dbUrl != null) {
				System.setProperty(Datastore.QUALITYCOLLECTOR_CONFIG_PREFIX + "connection.url", this.dbUrl);
			}
			if (this.dbUser != null) {
				System.setProperty(Datastore.QUALITYCOLLECTOR_CONFIG_PREFIX + "connection.username", this.dbUser);
			}
			if (this.dbPassword != null) {
				System.setProperty(Datastore.QUALITYCOLLECTOR_CONFIG_PREFIX + "connection.password", this.dbPassword);
			}
			if (this.dbDriverClass != null) {
				System.setProperty(Datastore.QUALITYCOLLECTOR_CONFIG_PREFIX + "connection.driver_class", this.dbDriverClass);
			}
		}
		
		private void rebuildDatastore() throws Throwable {
			this.setSystemParamsForDB();
			this.qualityCollectorRootAction.datastore.reInitializeDatabaseConnection();
		}		
	}	
	
	
	/*public static void main(String[] args) {
		new QualityCollectorRootAction().getMainPageDisplayDataFor("1");
	}*/
	
	public QualityCollectorRootAction() {
		this.descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptorOrDie(getClass());
		this.descriptor.setQualityCollectorRootAction(this);
		this.descriptor.setSystemParamsForDB();
		try {
			this.datastore = new ORMLiteDatastore();
			this.initAllServices();
		} catch (ExceptionInInitializerError e) {
			if (e.getException() instanceof SQLException) { // TODO was Hibernate Exception
				logger.log(Level.SEVERE, "DB connection not successful:");
			}
			logger.log(Level.SEVERE, "Fatal Server Error: " + e.getMessage());
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Fatal Server Error: " + t.getMessage());
		}
		this.addStandardActions();
	}

	public QualityCollectorRootAction(Datastore datastore) {
		this.datastore = datastore;
		this.descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptorOrDie(getClass());
		this.descriptor.setQualityCollectorRootAction(this);

		this.initAllServices();
		this.addStandardActions();
	}	

	private void initAllServices() {
		this.testExecutionSummaryService = new TestExecutionSummaryService(this.datastore);
		this.displayDataService = new DisplayDataService(this.testExecutionSummaryService);
		this.tableDataService = new TableDataService(this.testExecutionSummaryService);
		if (this.datastore != null) {
			this.testObjectVersionService = new TestObjectVersionService(this.datastore.getAllTestObjectVersions());
		}
		else {
			this.testObjectVersionService = new TestObjectVersionService(null);
		}
	}
	
	public String getIconFileName() {
		return QUALITY_COLLECTOR_DASHBOARD_ICONFILENAME;
	}

	public String getDisplayName() {
		return QUALITY_COLLECTOR_DASHBOARD_NAME;
	}

	public String getUrlName() {
		return QUALITY_COLLECTOR_DASHBOARD_URL;
	}

	/*public TableDataService getTableDataService() {
		return this.tableDataService;
	}*/
	
	@JavaScriptMethod
	public List<TestObjectVersion> getTestObjectVersions() {
		return this.testObjectVersionService.getTestObjectVersions();
	}

	@JavaScriptMethod
	public String getDefaultTestObjectVersionId() {
		return this.descriptor.getDefaultTestObjectVersionId();
	}

	@Override
	public String getSearchUrl() {
		return QUALITY_COLLECTOR_DASHBOARD_URL;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Descriptor getDescriptor() {
		return descriptor;
	}

	@JavaScriptMethod
	public MainPageDisplayDataDto getMainPageDisplayDataFor(
			String versionIdAsString) {

		return this.displayDataService.getMainPageDisplayDataDtoFor(this,
				this.testObjectVersionService.getTestObjectVersionForIdAsString(versionIdAsString));
	}	

	@Override
	public String getDescriptionFor(TestCaseExecution testCaseExecution) {
		return "";
	}
	
	@JavaScriptMethod
	public boolean isDatastoreSet() {
		return (this.datastore != null);
	}
	
	public ContextMenu doContextMenu(StaplerRequest request,
			StaplerResponse response) throws Exception {

		final ContextMenu menu = new ContextMenu();
		if (this.datastore != null) {
			for (Action action : this.getAllActions()) {
				menu.add(action.getUrlName(), action.getIconFileName(), action.getDisplayName());
			}
		}
		return menu;
	}

	private void addStandardActions() {
		this.addAction(new QualityCollectorTeamViewAction(this.tableDataService, this.testObjectVersionService));
		this.addAction(new QualityCollectorToolViewAction(this.tableDataService, this.testObjectVersionService));
		this.addAction(new QualityCollectorTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
		this.addAction(new QualityCollectorTeamToolViewAction(this.tableDataService, this.testObjectVersionService));
		this.addAction(new QualityCollectorTeamTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
		this.addAction(new QualityCollectorToolTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
		this.addAction(new QualityCollectorTeamToolTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
	}
	
	/*private void replaceStandardActions() {
		this.replaceAction(new QualityCollectorTeamViewAction(this.tableDataService, this.testObjectVersionService));
		this.replaceAction(new QualityCollectorToolViewAction(this.tableDataService, this.testObjectVersionService));
		this.replaceAction(new QualityCollectorTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
		this.replaceAction(new QualityCollectorTeamToolViewAction(this.tableDataService, this.testObjectVersionService));
		this.replaceAction(new QualityCollectorTeamTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
		this.replaceAction(new QualityCollectorToolTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
		this.replaceAction(new QualityCollectorTeamToolTestobjectViewAction(this.tableDataService, this.testObjectVersionService));
	}*/
}
