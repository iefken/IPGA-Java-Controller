# Introduction:
    Integration Project Group A: Java Controller

    * This project is meant to serve as an 'example' project that should make it clear on 
    how to use senders and receivers with the RabbitMQ protocol. (see: https://www.rabbitmq.com/)
    * This project is made as part of the 'Integration Project' course at Erasmus Hogeschool Brussel.
    (see: https://www.ehb.be/)

# Content:
    1. Requirements
    2. Installation Process
    3. Software dependencies
    4. Latest releases
    5. TFS-instructions
    6. Extra

# 1. Requirements

	* Make sure to connect to VPN
	* Make sure to read what every option does and needs before executing it!

# 2. Installation process

## 2.1. Get the files

    * git clone ssh://TODO
    * git clone https://github.com/iefken/IPGA-Java-Controller.git

## 2.2. Open the project locally

### 2.2.A When opening IntelliJ :
     => Import project...
     => select 'new directory'
     => ...
 
### 2.2.B When already in IntelliJ:
     => File
     => New
     => Project from Existing Sources
     => select 'new directory'
     => Maven external model
     => "AUTOMATICALLY DOWNLOAD SOURCES" select 'nieuwe directory' 
     => Next (x3)
     => Set local project name (only change if you know what you're doing...)
     => FINISH
 
     (=> maven depencies should be downloaded automatically... )
 
## 2.3. Build the project locally

    Run Receiver.java for registering Receiver to server, run Main.java for a CLI sender

    => On error, go to Main.java and Receiver.java and manually click the 'green run'-button once...

## 2.4. Open the project on your server

    Make sure your the file you want to push to the server works local!

### 2.4.A. Create the .jar file:
    Follow the directives on this link: 
    * https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html#package

OR in IntelliJ:

#### 2.4.A.1. Prepare the .jar:

    Select File | Project Structure.
    => New window: Under 'Project Settings' click 'Artifacts'.
    => Click the upper left 'green +' , click JAR, click 'From modules with dependies'. 
    => New window: Select class with main class (receiver) click oke. 
    => New window: You'll see the depencies you'll include, click oke again.
           
#### 2.4.A.2. Build the .jar: 

    Select Build | Build Artifacts. 
    Select theJarYouPrepared:jar and select 'Build'. 
    
    => This should start the build process into the "/out/" folder.

#### ! Intermezzo ! 
    TEST your .jar file in terminal (/powershell/cmd): 'java -jar jarFileName.jar'

#### 2.4.A.3. Make .jar runnable (OPTIONAL) 

    Select Run | Edit Configurations. 
    => New window: click new (green +) and select JAR Application.

    ==> Continue only if your .jar files runs successfully (it does the same as running the file/project does/what you want it does...)

### 2.4.B. Get the .jar on your server

#### 2.4.B.1. Put your .jar on your VCS: Git(hub)/TFS
    For putting the jar easily on your server I recommend you putting it on your VCS first
#### 2.4.B.2. Clone it onto your remote server
    In the directory you want to push it: 
    "git clone https://github.com/your-github-nickname/your-github-project-name git-files/"
    "git clone ssh://..."
    (the 'git-files' parameter is optional for naming the new repo the github-project will be pushed in, if it's empty, your-github-project-name will be used)

#### 2.4.B.3. Navigate to your .jar directory
    Run this line: 'java -jar yourJarFileName.jar'


# 3.	Software dependencies

    Any IDE will do to read and change the code, if you know how to handle the dependencies.
    This project uses Maven (see: https://mvnrepository.com/) as dependency manager and 
    should work on both Linux (debian) and Windows(10) when developing with the IntelliJ IDE (see: https://www.jetbrains.com/idea/). 
    ! If you have instructions for building this project in Eclipse or other IDE's I'll be glad to add them !

    To read and/or see the mysql database: go to the 'db-models' repository and use MySQLWorkbench (see: https://www.mysql.com/products/workbench/) to open the file.

# 4.	Latest releases

    16-05-18: v1.4: Main can 'mock' updates for  user, event, session and reservation messages
    14-05-18: v1.3: Integrated Google Calendar API (GCA) calls into receiver
    10-05-18: v1.2: Main can 'mock' new user, event, session and reservation messages
    08-05-18: v1.1: Receiver handles reservation messages for events and sessions
    07-05-18: v1.0: Receiver handles messages for user, event and session correctly
    06-05-18: v0.9: Receiver handles user messages
    03-05-18: v0.8: Added PingMessage
    01-05-18: v0.7: Added some Data and DAO classes
    30-04-18: v0.6: Added CLI for testing purposes
    29-04-18: v0.5: Added .jar instructions for serverdeployment
    28-04-18: v0.4:
     	- Added XmlMessage: XML => String
     	- Added HttpRequest: (2,3,4)
    27-04-18: v0.3: Added XmlMessage: String => Xml 
    26-04-18: v0.2: Added HttpRequest: (1)
    24-04-18: v0.1: Sender and receiver classes functional

Happy queueing!

# 5. Extra

## 5.1. Readme for TFS @ ehb.be

### A. Setup your SSH

#### A.1. Generate a SSH key if you have not one yet

    https://help.github.com/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent/

##### A.1.1. Create SSH key:
    ssh-keygen -t rsa -b 4096 -C "your_email@example.com"

##### A.1.2. start ssh-agent if not started:
    eval $(ssh-agent -s)

##### A.1.3. Add SSH key to ssh-agent:
    ssh-add ~/.ssh/id_rsa (or any other repo/filename you may have given)

#### A.2. Setup SSH key for use with your TFS:

    https://docs.microsoft.com/en-us/vsts/git/use-ssh-keys-to-authenticate?view=vsts

### B. Clone remote repository

#### B.1. Clone remote repository locally: 

    git clone ssh://<LINK>
    git clone ssh://dttfs2017.ehb.be:22/Integration%20Project%20-%20Groep%20A/_git/MONITORING

    (check 'git remote -v' to make sure it you're linked to your given url)

#### B.2. Checkout remote branch
    git checkout <branch_name>
    git checkout remotes/origin/DEVBranche_Ief

    (check 'git status' to make sure you're on your chosen branch

##### ¿? Don't know your branch name ¿?

    Check all branches linked to ssh://<LINK> with the following command:
    git branch -a

    And choose the one you want to checkout

#### B.3. Add local changes to next commit
    (add files in file repository now to add them to TFS in the next commit if you want)

    git add --all

#### B.4. Commit these local changes for next push

    git commit -m "Your commit message"

#### B.5. Push your local changes to your remote branch

    git push origin <branch_name>
    git push origin DEVBranch_Ief

May git commit what you submit!


## 5.2 PingMessage

### A. Add PingSender.java, PingMessage.java and PingStructure.java to your project 

    * Github: https://github.com/iefken/IPGA-Java-Controller/tree/master/src/main/java/AppLogic
    * TFS: 
    
### B. Add following code to parse the correct ping message to xml:

    static String getXmlForPingMessage(String messageType, SourceType Source_type) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, "", Source_type.toString());
        // set datastructure
        XmlMessage.PingStructure pingStructure = new XmlMessage.PingStructure();
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.PingMessage xmlPingMessage = new XmlMessage.PingMessage(header, pingStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlPingMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

### C. Add following code to your Sender.java

Or anywhere you want as long as you send the message to the monitor queue here

    public static String sendPingMessage(String xmlMessage,Helper.SourceType thisSourceType) throws IOException, TimeoutException, JAXBException {

        ConnectionFactory factory = new ConnectionFactory();

	    //DON'T CHANGE (monitor receives ping messages only this way):
        String TASK_QUEUE_NAME = "monitor-queue";

	    //set your creds here
        String username = "";
        String password = "";
        String virtualHost = "/";

        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(Helper.HOST_NAME_LINK);
        factory.setPort(Helper.PORT_NUMBER);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //publish to queue

        try {
            channel.basicPublish("", TASK_QUEUE_NAME, null, xmlMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        channel.close();
        connection.close();

        return " => Sending as '"+thisSourceType+"' to queue: '" + TASK_QUEUE_NAME + "' with message length: '" + xmlMessage.length() + "'";


    }

### 2.D. Add following code to your receiver.java
! This will start a new thread !

 For sending PingMessage every 'timeBetweenPings' milliseconds: 

        // Send pingmessage every 'timeBetweenPings' milliseconds
        int timeBetweenPings = 5000;

        // make new pingSender object
        PingSender pingSender = new PingSender(0, Helper.SourceType.Planning, timeBetweenPings);

        // setup new pingSender thread
        Thread pingThread = new Thread(pingSender);

        // start new pingSender thread
        pingThread.start();

