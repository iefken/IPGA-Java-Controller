package AppLogic;

import DatabaseLogic.*;
import GoogleCalendarApi.*;
import com.rabbitmq.client.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import static AppLogic.Helper.getSafeXmlProperty;
import static GoogleCalendarApi.GoogleCalenderApi.getCalendarService;

public class Receiver {

    private static int workCounter = 0;

    public static void main(String[] argv) throws Exception {

        // Set your source type here: this will determine what queue you will receive messages from!
        Helper.SourceType yourSourceType = Helper.SourceType.Planning;

        // method for showing some awefull cli design paint tests...
        Helper.receiverCliStartPaint(yourSourceType);

        //uncomment for sending pingmessage in a different thread
        Helper.startSendingPingMessages(10000,yourSourceType);

        //# Setup rabbitMQ connection
        ConnectionFactory factory = new ConnectionFactory();

        //https://www.rabbitmq.com/api-guide.html
        String username = "" + yourSourceType;
        String password = ("" + yourSourceType).toLowerCase();
        String virtualHost = "/";
        String TASK_QUEUE_NAME = ("" + yourSourceType).toLowerCase() + "-queue";

        //for localhost (you need to have a rabbit-mq server running for this)
        //factory.setHost("localhost");
        String hostName = "10.3.50.38";
        int portNumber = 5672;

        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostName);
        factory.setPort(portNumber);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //ONLY ex/quDECLARE(...) WHEN exchange/queue DOESN'T EXIST on server yet

        //channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

        channel.queueBind(TASK_QUEUE_NAME, Helper.EXCHANGE_NAME, "");

//        System.out.println(" [ooo] _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ [ooo]");
        System.out.println("\n*********************************************************************************");
        System.out.println("*       [...] Waiting with queue for messages. To exit press CTRL+C [...]       *");
        System.out.println("*********************************************************************************\n");

        final int[] numberOfMessage = {1};

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                //System.out.println("\n [x] Start");
                //System.out.println("\n [x] MessageStart:\n" + message);
                //System.out.println("\n [x] MessageEnd\n");
                try {
                    numberOfMessage[0] = Helper.handleNewMessage(message, numberOfMessage[0]);
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Error handling new message: "+e);
                } finally {
                    // method for showing some awfull cli design paint tests...
                    Helper.receiverMessageWaitPaint();
                }
                // "* [.i.] ** [@" + Helper.getCurrentDateTimeStamp() + "]
            }
        };
        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
    } //END of Receiver class
}
