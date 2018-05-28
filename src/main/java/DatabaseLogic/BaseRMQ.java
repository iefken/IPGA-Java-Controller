package DatabaseLogic;

import AppLogic.Helper;
import com.rabbitmq.client.*;

import java.io.IOException;

public class BaseRMQ {


    private static ConnectionFactory factory = new ConnectionFactory();
    private static Channel channel;
    private static Connection connection;
    private static Helper.SourceType yourSourceType = Helper.SourceType.Planning;

    private static String username = "" + yourSourceType;
    private static String password = ("" + yourSourceType).toLowerCase();
    private static String virtualHost = "/";
    private static String hostName = "10.3.50.38";
    private int portNumber = 5672;

    private static String TASK_QUEUE_NAME = ("" + yourSourceType).toLowerCase() + "-queue";
    //TASK_QUEUE_NAME = "test-queue"
    private static String currentExchangeName = "rabbitexchange";


    //GETTERS & SETTERS
    public static Channel getRmqChannel() {

        return channel;
    }
    private void setupRmqChannel(Connection connection)  {

        setRmqConnection(connection);
        try {
            this.channel = connection.createChannel();
            try {
                this.channel.queueBind(TASK_QUEUE_NAME, currentExchangeName, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            final int[] numberOfMessage = {1};
            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");

                    try {
                        numberOfMessage[0] = Helper.handleNewMessage(message, numberOfMessage[0]);
                    } catch (Exception e) {
                        System.out.println("Error handling new message: "+e);
                    } finally {
                        Helper.receiverMessageWaitPaint();
                    }
                }
            };
            Helper.receiverMessageWaitPaint();
            boolean autoAck = true; // acknowledgment is covered below
            channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static Connection getRmqConnection() {return connection; }

    private void setRmqConnection(Connection connection) {
        this.connection = connection;
    }
    public BaseRMQ() {
        try {

            factory.setUsername(username);
            factory.setPassword(password);
            factory.setVirtualHost(virtualHost);
            factory.setHost(hostName);
            factory.setPort(portNumber);

            Connection connection=factory.newConnection();
            setupRmqChannel(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
