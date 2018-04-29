# IPGA-Java-Controller
Integration Project Group A: Java Controller

Different options for running this controller on your local computer:

## Get the files

### A. With .zip file

1. Download .zip file containing the whole project
2. Unzip .zip file in a 'new directory'

### B. From github

1. 'git clone https://github.com/iefken/IPGA-Java-Controller.git YOUR_REPO_NAME'
Or download straight from github/iefken/IPGA-Java-Controller ...

## Open the project locally

Open IntelliJ : => import project => select 'new directory' => ...
 
When already in IntelliJ: => File => New => Project from Existing Sources =>  select 'new directory' => ...
 
THEN Choose: 
 
 Maven external model => "AUTOMATICALLY DOWNLOAD SOURCES" select 'nieuwe directory' 
 => Next (x3) => Set local project name (only change if you know what you're doing...) => FINISH
 
 ( => maven depencies should be downloaded automatically... )
 
4. Add src/main/java/logic/amqp-client... , slf4j-api-..., slf4j-simple... as library => Project Library

5. Build project

6. Run ReceiverPlanning.java for registering Receiver to server, run Main.java for a CLI sender

=> On error, go to Main.java and ReceiverPlanning.java en manually click the 'green run'-button once...

