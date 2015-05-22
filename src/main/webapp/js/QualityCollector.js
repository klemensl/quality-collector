function initTestObjectVersion(testObjectVersionId) {
 	 if (testObjectVersionId != "") {
 		 jQuery("#version_select").val(testObjectVersionId);
 	 }  
}

function summaryChartFilter(element) {
	return containsString(element.label,document.getElementsByName("filter_description")[0].value);
};

function containsString(string, searchstring) {
	var lcString=string.toLowerCase();
	var lcSearchString=searchstring.toLowerCase();

	return lcString.indexOf(lcSearchString)>=0;
};

function applyFilter() {
	table.columns(0).search(document.getElementsByName("filter_description")[0].value).draw();
    pieChart = document.getElementById('pieChart').getContext('2d');
	if (pieChartObject != null) {
		   pieChartObject.destroy();
	}	    
    var filteredSummaryChart = testObjectData.summaryChart.filter(summaryChartFilter);
    pieChartObject = new Chart(pieChart).Pie(filteredSummaryChart,{responsive : false, animationEasing : "easeOutQuad", segmentShowStroke : false}); 	
}

function loadTableData() {
		action.getTestExecutionSummaryTable(sessionStorage.testObjectVersionID,function(t) {
	   	testObjectData = t.responseObject();
	   	table.clear();
	    pieChart = document.getElementById('pieChart').getContext('2d');
		if (pieChartObject != null) {
			   pieChartObject.destroy();
		}	   	
	   	if (testObjectData != null) {
		   	if (testObjectData.testObjectVersionName != "") {
		   	   $j('h3 span').text("Testobject Version: " + testObjectData.testObjectVersionName);
		   	} 
		    table.rows.add(testObjectData.tableData).columns(0).search(document.getElementsByName("filter_description")[0].value).draw();
		    jQuery("td").css("vertical-align", "middle");
		    jQuery("td").not("td:first-child").css("text-align", "center");
		    jQuery("td:first-child").css({"font-weight":"bold","padding-left": "3%"});	    
		    var filteredSummaryChart = testObjectData.summaryChart.filter(summaryChartFilter);
		    pieChartObject = new Chart(pieChart).Pie(filteredSummaryChart,{responsive : false, animationEasing : "easeOutQuad", segmentShowStroke : false}); 
		    var datasetId;
		    jQuery("canvas[id^='Last ']").each(function(index, value) { 
		    	var canvas_id = $j(this).attr('id');
		    	datasetId = $j(this).find("param[name='tablechart_id']").attr('value');
		    	var chartDrawing = document.getElementById(canvas_id).getContext('2d');   	
		    	var chart = new Chart(chartDrawing).StackedBar(testObjectData.tableCharts[datasetId][0], {responsive : false, barValueSpacing : 0, multiTooltipTemplate: "<%= datasetLabel %> - <%= value %>", showHorizontalLegend: false}); 
		    });
	   	}
	});
};	
		
function loadMainViewData() {

	var versionId = jQuery("#version_select option:selected").val();
	if (window.sessionStorage){
		sessionStorage.testObjectVersionID = versionId;
 	}	
	rootAction.getMainPageDisplayDataFor(versionId,function(t) {
	   	testObjectData = t.responseObject();
	    var chartDrawing = document.getElementById("mainChart").getContext('2d');  
		if (mainViewChart != null) {
			mainViewChart.destroy();
		}	
		if (testObjectData != null) {
			mainViewChart = new Chart(chartDrawing).StackedBar(testObjectData.mainChart, {responsive : false, barValueSpacing : 0, multiTooltipTemplate: "<%= datasetLabel %> - <%= value %>", barShowStroke: true});   
		    var headlineText = testObjectData.mainSummary.failureCount==0 ? "Last Execution " :"Last Execution FAILED " 
		    				   +	"on " + testObjectData.mainSummary.dateFormated + ": "
		    				   + (testObjectData.mainSummary.testCount
		    						   -testObjectData.mainSummary.failureCount
		    						   -testObjectData.mainSummary.inconclusiveCount
		    						   -testObjectData.mainSummary.ignoredCount)
		    				   +"/"+testObjectData.mainSummary.testCount;
		    document.getElementById("overviewExecutionHeadline").innerHTML=headlineText;
		    document.getElementById("overviewExecutionRunCount").innerHTML="Tests ok: "+(testObjectData.mainSummary.testCount
																						   -testObjectData.mainSummary.failureCount
																						   -testObjectData.mainSummary.inconclusiveCount
																						   -testObjectData.mainSummary.ignoredCount);
		    document.getElementById("overviewExecutionIgnoredCount").innerHTML="Tests ignored: "+testObjectData.mainSummary.ignoredCount;
		    document.getElementById("overviewExecutionInconclusiveCount").innerHTML="Tests inconclusive: "+testObjectData.mainSummary.inconclusiveCount;
		    document.getElementById("overviewExecutionFailureCount").innerHTML="Tests failed: "+testObjectData.mainSummary.failureCount;
		    if (testObjectData.mainSummary.failureCount != 0) {
		    	document.getElementById("overviewExecutionHeadline").className="quality_collector_headline_failure";
		    }
		    else if (testObjectData.mainSummary.inconclusiveCount != 0) {
		    	document.getElementById("overviewExecutionHeadline").className="quality_collector_headline_inconclusive";
		    }
		    else if (testObjectData.mainSummary.ignoredCount != 0) {
		    	document.getElementById("overviewExecutionHeadline").className="quality_collector_headline_ignored";
		    }
		    else {
		    	document.getElementById("overviewExecutionHeadline").className="quality_collector_headline_ok";
		    }
		    document.getElementById("overviewExecutionRunCount").className= (testObjectData.mainSummary.testCount
																			   -testObjectData.mainSummary.failureCount
																			   -testObjectData.mainSummary.inconclusiveCount
																			   -testObjectData.mainSummary.ignoredCount) == 0
		    																	? "quality_collector_summary_text_no_ok":"quality_collector_summary_text_ok";
		    document.getElementById("overviewExecutionIgnoredCount").className= testObjectData.mainSummary.ignoredCount==0 ? "quality_collector_summary_text_no_ignored":"quality_collector_summary_text_ignored";
		    document.getElementById("overviewExecutionInconclusiveCount").className= testObjectData.mainSummary.inconclusiveCount==0 ? "quality_collector_summary_text_no_inconclusive":"quality_collector_summary_text_inconclusive";
		    document.getElementById("overviewExecutionFailureCount").className= testObjectData.mainSummary.failureCount==0 ? "quality_collector_summary_text_no_failure":"quality_collector_summary_text_failure";
		} 
		else {
		    document.getElementById("overviewExecutionHeadline").innerHTML="No Tests run";
		    document.getElementById("overviewExecutionRunCount").innerHTML="Tests ok: 0";
		    document.getElementById("overviewExecutionIgnoredCount").innerHTML="Tests ignored: 0";
		    document.getElementById("overviewExecutionInconclusiveCount").innerHTML="Tests inconclusive: 0";
		    document.getElementById("overviewExecutionFailureCount").innerHTML="Tests failed: 0";
		}
	});
};

jQuery.extend(jQuery.fn.dataTable.moment = function ( format, locale ) {
    var types = jQuery.fn.dataTable.ext.type;
 
    // Add type detection
    types.detect.unshift( function ( d ) {
        return moment( d, format, locale, true ).isValid() ?
            'moment-'+format :
            null;
    } );
 
    // Add sorting method - use an integer for the sorting
    types.order[ 'moment-'+format+'-pre' ] = function ( d ) {
        return moment( d, format, locale, true ).unix();
    };
});

function compareRunResults(runA,runB) {
    var aClass = runA.split("class=\"")[1].split("\"")[0];
    var aRuns = parseInt(runA.split("/")[1].split("<")[0]);
    var bClass = runB.split("class=\"")[1].split("\"")[0];
    var bRuns = parseInt(runB.split("/")[1].split("<")[0]); 
    if (aClass == bClass) {
    	if (aRuns>bRuns) {
    		return 1;
    	}
    	if (aRuns<bRuns) {
    		return -1;
    	}
    	return 0;
    }
    if (aClass == "quality_collector_failure") {
    	return 1;
    }  
    if (bClass == "quality_collector_failure") {
    	return -1;
    }
    if (aClass == "quality_collector_inconclusive") {
    	return 1;
    }
    if (bClass == "quality_collector_inconclusive") {
    	return 1;
    } 
    if (aClass == "quality_collector_ignored") {
    	return 1;
    }        
    return -1;
}

jQuery.extend( jQuery.fn.dataTableExt.oSort, {
    "run_result-asc": function ( a, b ) {
    	return compareRunResults(a,b);
    },
 
    "run_result-desc": function ( a, b ) {
        return compareRunResults(b,a);
    }
} );