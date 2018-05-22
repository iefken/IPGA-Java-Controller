package AppLogic;

import DatabaseLogic.*;

import com.rabbitmq.client.*;

public class Receiver {

    private static int workCounter = 0;

    public static void startReceiver() throws Exception {

        // this initializes rabbit mq connection with variables set in BaseRMQ().setRmqChannel();
        Channel channel = new BaseRMQ().getRmqChannel();
    }

    public static void main(String[] argv) throws Exception {

        startReceiver();


    } //END of Receiver class
}