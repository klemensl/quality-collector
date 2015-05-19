Installation:

To use Hibernate, the Log4J Jar of Jenkins has to be updated to version 1.2.17. The jar file is located in Jenkins/war/WEB-INF/lib.
The next step is to upload the plugin, this is done in "manage Jenkins"/"manage Plugins" in the tap "Advanced". After the plugin is uploaded, there should be a new entry "Quality Collector Dashboard" in the Jenkins main menu.
The  plugin still needs some configuration, this is done in "manage Jenkins"/"manage System". There is a new segment "Quality Collector" to configure the DB parameters. After saving a working connection, the plugin is ready to use.

Some comment to the used js librarys: Chart.js and Chart.StackedBar.js are customized, QualityCollector.js is a completely new library for the plugin.