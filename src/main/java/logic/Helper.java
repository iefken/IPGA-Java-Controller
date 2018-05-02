package Logic;

import okhttp3.*;
import HttpRequest.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Helper {

    enum EntityType {Visitor, Admin, Responsible}

    ;

    enum SourceType {Front_End, Planning, Monitoring, Kassa, CRM, Facturatie}

    ;

    String TASK_QUEUE_NAME = "planning-queue";
    String EXCHANGE_NAME = "rabbitexchange";
    String HOST_NAME_LINK = "10.3.50.38";
    int PORT_NUMBER = 5672;

    //For setting CLI options in main
    static String[] getOptions() {

        //Add CLI options here (a.b. : a: choice, b: sort of message
        String[] options = {
                "[01.x] Create new Event without UUID (createUuidRecord,EventMessage)",
                "[02.V] Create new Session without UUID (createUuidRecord,SessionMessage)",
                "[03.x] Create new User without UUID (createUuidRecord,UserMessage)",
                "[04.x] Create new Event with UUID (insertUuidRecord,EventMessage)",
                "[05.V] Create new Session with UUID (insertUuidRecord,SessionMessage)",
                "[06.x] Create new User with UUID (insertUuidRecord,UserMessage)",
                "[07.x] Update Event (UpdateUuidRecordVersion,EventMessage)",
                "[08.V] Update Session (UpdateUuidRecordVersion,SessionMessage)",
                "[09.x] Update User (UpdateUuidRecordVersion,UserMessage)",
                "[10.x] Add User to Event",
                "[11.V] Add User to Session",
                "[12.V] Get all UUID's from UUID manager",
                "[13.x] Fill in a (test message)",
                "[14.1.old.v] New Reservation_Session object without UUID",
                "[15.2.old.v] New Reservation_Session object with UUID: after create / update entity: normally when a new message from another team is received",
                "[16.1.old.v] New Session object without UUID",
                "[17.2.old.v] New Session object with UUID: after create / update entity:  normally when a new message from another team is received",
                "[18.3.old.v] Alter existing entity and update UUID mgr: update event, update session, add User to session,...",
                "[19.4.old.v] Alter record directly in UUID (select on UUID and SOURCE)"

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

        if (method == "post") {
            request = new Request.Builder()
                    .url(myUrl)
                    .post(body)
                    .build();

        } else if (method == "put") {

            request = new Request.Builder()
                    .url(myUrl)
                    .put(body)
                    .build();

        } else if (method == "get") {

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

    //reservationMessage: User- UUID, Session-UUID, Status(isActive), Timestamp
    static String getXmlForReservation(String messageType, String description, SourceType Source_type, int reservationId, String reservationUUID, String userUUID, String sessionUUID) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationStructure reservationStructure = new XmlMessage.ReservationStructure(reservationId,reservationUUID,userUUID, sessionUUID, "1", Helper.getCurrentDateTimeStamp(), messageType.split("Message")[0]);
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationMessage xmlReservationMessage = new XmlMessage.ReservationMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    //sessionMessage: UUID, Name, Date, Start-time, End-time, Speaker, Local, Type (workshop, speech,..), Timestamp
    static String getXmlForNewSession(String messageType, String description, SourceType Source_type, String sessionUUID, String eventUUID, String name, int maxAttendees, String dateTimeStart, String dateTimeEnd, String speaker, String local, String type, int sessionStatus)throws JAXBException
    {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.SessionStructure sessionStructure = new XmlMessage.SessionStructure(sessionUUID, eventUUID, name, dateTimeStart, dateTimeEnd, speaker, maxAttendees, local, type, sessionStatus, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (SessionStructure) in message klasse
        XmlMessage.SessionMessage sessionMessage = new XmlMessage.SessionMessage(header, sessionStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = sessionMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }
    //eventMessage: UUID, Name, Start-dateTime, End-dateTime, Location, Timestamp


    static String getOurXmlMessage(String messageType, String description, SourceType Source_type, String UUID) throws JAXBException {

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.MessageStructure messageStructure = new XmlMessage.MessageStructure(UUID, "1", messageType, Helper.getCurrentDateTimeStamp());
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
