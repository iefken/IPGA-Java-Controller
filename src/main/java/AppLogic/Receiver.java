package AppLogic;

import DatabaseLogic.*;

import com.rabbitmq.client.*;

public class Receiver {

    public static void startReceiver() {

        // this initializes rabbit mq connection with variables set in BaseRMQ().setRmqChannel();
        Channel channel = new BaseRMQ().getRmqChannel();
    }

    public static void main(String[] argv) throws Exception {

        startReceiver();

    } //END of Receiver class
}