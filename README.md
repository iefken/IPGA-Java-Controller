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

=> On error, go to Main.java and ReceiverPlanning.java and manually click the 'green run'-button once...

## Open the project on your server

Make sure your the file you want to push to the server works local!

### 1. Create the .jar file:
Follow the directives on this link: 
https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html#package

OR
 
in IntelliJ:

#### a. Prepare the .jar:

 Select File | Project Structure.
   => New window: Under 'Project Settings' click 'Artifacts'.
      => Click the upper left 'green +' , click JAR, click 'From modules with dependies'. 
         => New window: Select class with main class (receiver) click oke. 
           => New window: You'll see the depencies you'll include, click oke again.
           
#### b. Build the .jar: 

Select Build | Build Artifacts. Select theJarYouPrepared:jar and select 'Build'. 
=> This should start the build process into the "/out/" folder.

#### c. Make .jar runnable: 

select Run | Edit Configurations. 
  => New window: click new (green +) and select JAR Application.

### ! Intermezzo ! TEST your .jar file in terminal: 'java -jar jarFileName.jar'

==> Continue only if your .jar files runs successfully (it does the same as running the file/project does/what you want it does...)

### 2. Get the .jar on your server

#### a. Put your .jar on your Git(hub)
For putting the jar easily on your server I recommend you putting it on your github first
#### b. Clone it onto your remote server
In the directory you want to push it: 'git clone https://github.com/your-github-nickname/your-github-project-name git-files/' 
(the 'git-files' parameter is optional for naming the new repo the github-project will be pushed in, if it's empty, your-github-project-name will be used)

#### c. Navigate to your .jar directory
and run this line: 'java -jar yourJarFileName.jar'

