# Send PingMessages in a separate thread on your receiver

### 1. Add PingSender.java, PingMessage.java and PingStructure.java to your project 

https://github.com/iefken/IPGA-Java-Controller/tree/master/src/main/java/AppLogic

### 2. Add following code to parse the correct ping message to xml:

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

### 3. Add following code to your Sender.java to send PingMessage to monitor only
#### Or anywhere you want as long as you send the message to the monitor queue here

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

### 4. Add following code to your receiver to send PingMessages for a chosen interval: 

        // # Send pingmessage every 'timeBetweenPings' milliseconds
        int timeBetweenPings = 5000;

        // ## make new pingSender object
        PingSender pingSender = new PingSender(0, Helper.SourceType.Planning, timeBetweenPings);

        // ## setup new pingSender thread
        Thread pingThread = new Thread(pingSender);

        // ## start new pingSender thread
        pingThread.start();




