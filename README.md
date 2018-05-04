#Introduction:
Integration Project Group A: Java Controller

This project is meant to serve as an 'example' project that should make it clear on how to use senders and receivers with the RabbitMQ protocol.
(see: https://www.rabbitmq.com/)
This project is made as part of the 'Integration Project' course at Erasmus Hogeschool Brussel.
(see: https://www.ehb.be/)

#Getting Started

1.	Requirements

	* Make sure to connect to VPN
	* Make sure to read what every option does and needs 		before executing it!

2.	Installation process

## 1. Get the files

git clone ssh://TODO
git clone https://github.com/iefken/IPGA-Java-Controller.git

### 1.A. Open the project locally

#### 1.A.1.A When opening IntelliJ :
 => Import project...
 => select 'new directory'
 => ...
 
#### 1.A.1.B When already in IntelliJ:
 => File
 => New
 => Project from Existing Sources
 => select 'new directory'
 => Maven external model
 => "AUTOMATICALLY DOWNLOAD SOURCES" select 'nieuwe directory' 
 => Next (x3)
 => Set local project name (only change if you know what you're doing...)
 => FINISH
 
 ( => maven depencies should be downloaded automatically... )
 
#### 1.A.2. Build project

#### 1.A.3. Run Receiver.java or Main.java to test the build

=> On error, go to Main.java and Receiver.java and manually click the 'green run'-button once...

## 1.B. Open the project on your server

Make sure your the file you want to push to the server works local!

### 1.B.1.A. Create the .jar file:
Follow the directives on this link: 
https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html#package

OR
 
in IntelliJ:

#### 1.B.1.B.a. Prepare the .jar:

 Select File | Project Structure.
   => New window: Under 'Project Settings' click 'Artifacts'.
      => Click the upper left 'green +' , click JAR, click 'From modules with dependies'. 
         => New window: Select class with main class (receiver) click oke. 
           => New window: You'll see the depencies you'll include, click oke again.
           
#### 1.B.1.B.b. Build the .jar: 

Select Build | Build Artifacts. Select theJarYouPrepared:jar and select 'Build'. 
=> This should start the build process into the "/out/" folder.

### ! Intermezzo ! TEST your .jar file in terminal: 'java -jar jarFileName.jar'

#### OPTIONAL: 1.B.1.B.c. Make .jar runnable: 

select Run | Edit Configurations. 
  => New window: click new (green +) and select JAR Application.

==> Continue only if your .jar files runs successfully (it does the same as running the file/project does/what you want it does...)

### 1.B.2. Get the .jar on your server

#### 1.B.2.a. Put your .jar on your VCS: Git(hub)/TFS
For putting the jar easily on your server I recommend you putting it on your VCS first
#### 1.B.2.b. Clone it onto your remote server
In the directory you want to push it: "git clone https://github.com/your-github-nickname/your-github-project-name git-files/" //"git clone ssh://..."
(the 'git-files' parameter is optional for naming the new repo the github-project will be pushed in, if it's empty, your-github-project-name will be used)

#### 1.B.2.c. Navigate to your .jar directory
and run this line: 'java -jar yourJarFileName.jar'


3.	Software dependencies

To read and change the code any IDE will do if you know how to handle the dependencies.
This project uses Maven (see: https://mvnrepository.com/) as dependency manager and should work on both Linux (debian) and Windows(10) when developing with the IntelliJ IDE (see: https://www.jetbrains.com/idea/). 
! If you have instructions for building this project in Eclipse or other IDE's I'll be glad to add them !

To read and/or see the mysql database: go to the 'db-models' repository and use MySQLWorkbench (see: https://www.mysql.com/products/workbench/) to open the file.

4.	Latest releases

03-04-18: v0.8: Added PingMessage
01-04-18: v0.7: Added some Data and DAO classes
30-04-18: v0.6: Added CLI for testing purposes
29-04-18: v0.5: Added .jar instructions for serverdeployment
28-04-18: v0.4: 	- Added XmlMessage: XML => String
			- Added HttpRequest: (2,3,4)
27-04-18: v0.3: Added XmlMessage: String => Xml 
26-04-18: v0.2: Added HttpRequest: (1)
24-04-18: v0.1: Sender and receiver classes functional

# Happy queueing!
