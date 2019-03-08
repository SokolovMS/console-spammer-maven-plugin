# console-spammer-maven-plugin
Allows you to simulate your work and say that you are waiting for the build to be completed.

### Description
Plugin has 1 goal (spam) and 2 parameters (durationInSeconds and pathToFile).  
During `durationInSeconds` it will read data from `pathToFile` and will redirect it line by line to maven output. If file ends - plugin will read it again and again.

### Features
*  [DEBUG], [INFO], [WARN] and [ERROR] tags are supported in the beginning of the text lines
*  Random time sleep interval is presented after each print
*  Random time sleep interval ranges between supported and not supported tags are different

### How to start
1. Download
2. Build parent project
   
   >  mvn clean install  
   
   That will install plugin to your local maven repo and allow you to work with it goal directly in the future

### Example of usage if plugin is installed to local maven repo
>  mvn com.keshasosiska:console-spammer-maven-plugin:1.0-SNAPSHOT:spam -DdurationInSeconds=30
