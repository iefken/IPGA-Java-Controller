package logic;

import okhttp3.*;
import HttpRequest.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Helper {

    enum EntityType {Visitor, Admin, Responsible};
    enum SourceType {Front_End, Planning, Monitoring, Kassa, CRM, Facturatie};

    String TASK_QUEUE_NAME = "planning-queue";
    String EXCHANGE_NAME = "rabbitexchange";
    String HOST_NAME_LINK = "10.3.50.38";
    int PORT_NUMBER = 5672;

    //For setting CLI options in main
    static String[] getOptions() {

        //Add CLI options here (a.b. : a: choice, b: sort of message
        String[] options = {
                "1.1. New Event object without UUID",
                "2.2. New Event object with UUID: after create / update entity: normally when a new message from another team is received",
                "3.1. New session object without UUID",
                "4.2. New session object with UUID: after create / update entity:  normally when a new message from another team is received",
                "5.3. Alter existing entity and update UUID mgr: update event, update session, add User to session,...",
                "6.4. Alter record directly in UUID (select on UUID and SOURCE)",
                "7o. Get all UUID's from UUID manager",
                "8x. Fill in a (test message)",
                "9x. Prefab UUID message: list events (doesn't work)"
        };
        return options;
    }

    static String httpGetAllRecords(int limit) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        //HttpRequest.UUID_createUuidRecord myLocalUUID_createUuidRecordObject = new HttpRequest.UUID_createUuidRecord(Entity_sourceId, Entity_type, Source_type);

        //post request

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/getall";
        String json = "";
                //myLocalUUID_createUuidRecordObject.toJSONString();

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "get");

        //(handle request)
        //System.out.println("[i] In String httpGetAllRecords(): myLocalUUID_createUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    static String httpPostCreateUuidRecord(int Entity_sourceId, EntityType Entity_type, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        UUID_createUuidRecord myLocalUUID_createUuidRecordObject = new UUID_createUuidRecord(Entity_sourceId, Entity_type, Source_type);

        //post request

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/createUuidRecord";
        String json = myLocalUUID_createUuidRecordObject.toJSONString();

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "post");

        //(handle request)
        //System.out.println("[i] In String httpPostGetNewUuid(): myLocalUUID_createUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    static String httpPostInsertUuidRecord(String UUID, int Entity_sourceId, EntityType Entity_type, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        UUID_insertUuidRecord myLocalUUID_insertUuidRecordObject = new UUID_insertUuidRecord(Entity_sourceId, Entity_type, Source_type, UUID, 1);

        //post request
        // PHP: still inserts new record with new UUID while

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/insertUuidRecord";
        String json = myLocalUUID_insertUuidRecordObject.toJSONString();

        System.out.println("json to be sent for httpPostInsertUuidRecord: " + json);

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "post");

        //(handle request)
        //System.out.println("[i] In String httpPostUpdateUuidByUuid(): myLocalUUID_insertUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    static String httpPutUpdateUuidRecordVersion(String UUID, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_updateUuidRecordVersion(String myUrl, String UUID, logic.Sender.SourceType Source_type)
        UUID_updateUuidRecordVersion myLocalUUID_updateUuidRecordObject = new UUID_updateUuidRecordVersion(UUID, Source_type);


        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/updateUuidRecordVersion";
        String json = myLocalUUID_updateUuidRecordObject.toJSONString();

        //System.out.println("json: " + json);

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "put");

        //System.out.println("\n[i] In String httpPutUpdateUuidRecordVersion(): myLocalUUID_updateUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    static String httpPutUpdateUuidRecordVersionB(String UUID, int Entity_version, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_updateUuidRecordVersion(String myUrl, String UUID, logic.Sender.SourceType Source_type)
        UUID_updateUuidRecordVersionB myLocalUUID_updateUuidRecordObject = new UUID_updateUuidRecordVersionB(UUID, Entity_version, Source_type);

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/updateUuidRecordVersionB";
        String json = myLocalUUID_updateUuidRecordObject.toJSONString();
        System.out.println("json: " + json);


        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "put");

        System.out.println("\n[i] In String httpPutUpdateUuidRecordVersionB(): myLocalUUID_updateUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    static String doHttpRequest(String myUrl, String json, String method) throws IOException {


        MediaType JSON = MediaType.parse("application/json");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = null;

        if(method=="post")
        {
            request = new Request.Builder()
                    .url(myUrl)
                    .post(body)
                    .build();

        }else if(method=="put") {

            request = new Request.Builder()
                    .url(myUrl)
                    .put(body)
                    .build();

        }else if(method=="get"){

            request = new Request.Builder()
                    .url(myUrl)
                    .build();
        } else {

            return "Something went wrong in the logic.Helper: method not correct";

        }

        try (Response response = client.newCall(request).execute()) {

//            String thisResponse = response.body().string();

            return response.body().string();

        } catch (Exception e) {

            System.out.println(e.toString());
            return e.toString();
        }


    }

    static String getOurXml(String messageType, String description, SourceType Source_type, String userUUID, String sessionUUID) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, description+", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationStructure reservationStructure = new XmlMessage.ReservationStructure(userUUID, sessionUUID, "1", messageType, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationMessage xmlReservationMessage = new XmlMessage.ReservationMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    static String getOurXml2(String messageType, String description, SourceType Source_type, String UUID) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, description+", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.MessageStructure messageStructure = new XmlMessage.MessageStructure(UUID,"1", messageType, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.MessageMessage xmlReservationMessage = new XmlMessage.MessageMessage(header, messageStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    //https://stackoverflow.com/a/8345074
    static String getCurrentDateTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }


}
