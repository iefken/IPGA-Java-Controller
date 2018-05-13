package AppLogic;

import DatabaseLogic.*;
import okhttp3.*;
import HttpRequest.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Helper {

    //enum EntityType {VISITOR, EMPLOYEE, ADMIN, SPONSOR, SPEAKER, CONSULTANT}

    enum EntityType {EMPTY, USER, EVENT, SESSION, RESERVATION_EVENT, RESERVATION_SESSION, PRODUCT, PURCHASE}

    enum SourceType {Front_End, Planning, Monitor, Kassa, CRM, Facturatie}

    //String TASK_QUEUE_NAME = "planning-queue";
    String EXCHANGE_NAME = "rabbitexchange";
    String HOST_NAME_LINK = "10.3.50.38";
    int PORT_NUMBER = 5672;

    //For setting CLI options in main
    static String[] getOptions() {

        //Add CLI options here (a.b. : a: choice, b: sort of message
        String[] options = {
                "[01.V] Create new User without UUID (Front-End Call)",
                "[02.V] Create new Event without UUID (Front-End Call)",
                "[03.V] Create new Session without UUID (Front-End Call)",
                "[04.V] Create new Reservation_Event: Add User to Event",
                "[05.V] Create new Reservation_Session: Add User to Session",
                "[06.V] Get all UUID's from UUID manager",
                "[07.x] updateUuidRecordVersion",
                "[08.x] updateUuidRecordVersionB",
                "[09.V] List upcoming events (Google calendar 1) *authorize not yet included",
                "[10.V] Create new event (Google calendar 2)",
                "[11.x] Mock XML message",
                "/[12.x] /",
                "/[13.x] /New Session with UUID",
                "/[14.x] /New Reservation_Session with UUID"

        };
        return options;
    }

    // UUID: GET: all
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

    // UUID: POST: create
    static String httpPostCreateUuidRecord(int Entity_sourceId, AppLogic.Helper.EntityType Entity_type, SourceType Source_type) throws IOException {

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

    // UUID: POST: insert
    static String httpPostInsertUuidRecord(String UUID, int Entity_sourceId, EntityType Entity_type, SourceType Source_type) throws IOException {

        //make new object for HttpRequest.UUID_createUuidRecord(int source_id, EntityType thisEntityType, MessageSource thisMessageSource)
        UUID_insertUuidRecord myLocalUUID_insertUuidRecordObject = new UUID_insertUuidRecord(Entity_sourceId, Entity_type, Source_type, UUID, 1);

        //post request
        // PHP: still inserts new record with new UUID while

        String url = "http://" + HOST_NAME_LINK + ":8010/public/index.php/insertUuidRecord";
        String json = myLocalUUID_insertUuidRecordObject.toJSONString();

        //System.out.println("json to be sent for httpPostInsertUuidRecord: " + json);

        String myLocalUUID_Response_JSON_String = doHttpRequest(url, json, "post");

        //(handle request)
        //System.out.println("[i] In String httpPostUpdateUuidByUuid(): myLocalUUID_insertUuidRecordObject: " + myLocalUUID_Response_JSON_String);

        return myLocalUUID_Response_JSON_String;

    }

    // UUID: PUT: update1
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

    // UUID: PUT: update2
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

    // UUID: General HttpRequest
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


    // User: (params) => XML
    static String getXmlForNewUser(String xmlHeaderDescription, SourceType Source_type, String userUUID, String lastName, String firstName, String phoneNumber, String email, String street, String houseNr, String city, String postalCode, String country, String company, String type, int entity_version, int active, String timestamp) throws JAXBException {

        String messageType = "userMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.Userstructure userStructure = new XmlMessage.Userstructure(userUUID, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, type, entity_version, active, timestamp);
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.UserMessage xmlUserMessage = new XmlMessage.UserMessage(header, userStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlUserMessage.generateXML();


        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;

    }

    // User: Object => XML
    static String getXmlFromUserObject(String xmlHeaderDescription, SourceType Source_type, User user) throws JAXBException {

        String userUUID = user.getUuid();
        //System.out.println("HELPER: userUUID: "+userUUID);
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();
        String street = user.getStreet();
        String houseNr = user.getHouseNr();
        String city = user.getCity();
        String postalCode = user.getPostalCode();
        String country = user.getCountry();
        String company = user.getCompany();
        String type = user.getUserType();
        int entityVersion = user.getEntityVersion();
        int active = user.getActive();
        String timestamp = user.getTimestamp();

        String messageType = "userMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.Userstructure userStructure = new XmlMessage.Userstructure(userUUID, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, type, entityVersion, active, timestamp);
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.UserMessage xmlUserMessage = new XmlMessage.UserMessage(header, userStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlUserMessage.generateXML();


        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;

    }

    // User: XML => Object
    static User getUserObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        User userObject = null;

        String uuid = "false";
        String lastName = "false";
        String firstName = "false";
        String phoneNumber = "false";
        String email = "false";
        String street = "false";
        String houseNr = "false";
        String city = "false";
        String postalCode = "false";
        String country = "false";
        String company = "false";
        String userType = "false";
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        uuid = getSafeXmlProperty(xmlMessage, "uuid");
        if (uuid == "false") {

            uuid = getSafeXmlProperty(xmlMessage, "UUID");
            if (uuid == "false") {

                uuid = getSafeXmlProperty(xmlMessage, "userUuid");

                if (uuid == "false") {
                    uuid = getSafeXmlProperty(xmlMessage, "userUUID");

                    if (uuid == "false") {
                        uuid = getSafeXmlProperty(xmlMessage, "Uuid");
                        if (uuid == "false") {
                            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
                            allGood = false;
                        }
                    }
                }
            }
        }
        lastName = getSafeXmlProperty(xmlMessage, "lastName");
        if (lastName == "false") {

            System.out.println(" [!!!] ERROR: No lastName found in XML: ");
            allGood = false;

        }
        firstName = getSafeXmlProperty(xmlMessage, "firstName");
        if (firstName == "false") {

            System.out.println(" [!!!] ERROR: No firstName found in XML: ");
            allGood = false;

        }
        phoneNumber = getSafeXmlProperty(xmlMessage, "phoneNumber");
        if (phoneNumber == "false") {

            System.out.println(" [!!!] ERROR: No phoneNumber found in XML: ");
            allGood = false;

        }
        email = getSafeXmlProperty(xmlMessage, "email");
        if (email == "false") {

            System.out.println(" [!!!] ERROR: No email found in XML: ");
            allGood = false;

        }
        street = getSafeXmlProperty(xmlMessage, "street");
        if (street == "false") {

            System.out.println(" [!!!] ERROR: No street found in XML: ");
            allGood = false;

        }
        houseNr = getSafeXmlProperty(xmlMessage, "houseNr");
        if (houseNr == "false") {

            System.out.println(" [!!!] ERROR: No houseNr found in XML: ");
            allGood = false;

        }
        city = getSafeXmlProperty(xmlMessage, "city");
        if (city == "false") {

            System.out.println(" [!!!] ERROR: No city found in XML: ");
            allGood = false;

        }
        postalCode = getSafeXmlProperty(xmlMessage, "postalCode");
        if (postalCode == "false") {

            System.out.println(" [!!!] ERROR: No postalCode found in XML: ");
            allGood = false;

        }
        country = getSafeXmlProperty(xmlMessage, "country");
        if (country == "false") {

            System.out.println(" [!!!] ERROR: No country found in XML: ");
            allGood = false;

        }
        company = getSafeXmlProperty(xmlMessage, "company");
        if (company == "false") {

            System.out.println(" [!!!] ERROR: No company found in XML: ");
            allGood = false;

        }
        //type = Helper.EntityType.valueOf(getSafeXmlProperty(xmlMessage, "type"));
        userType = getSafeXmlProperty(xmlMessage, "userType");

        if (userType == "false") {

            userType = getSafeXmlProperty(xmlMessage, "type");

            if (userType == "false") {

                userType = getSafeXmlProperty(xmlMessage, "Type");

                if (userType == "false") {

                    System.out.println(" [!!!] ERROR: No userType found in XML: ");
                    allGood = false;
                }
            }

        }
        entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        if (entityVersion == 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        try {
            active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        } catch (NumberFormatException e) {
            //e.printStackTrace();

            //catch boolean instead of int

            if(getSafeXmlProperty(xmlMessage, "active")=="true"){
                active=1;
            }else if(getSafeXmlProperty(xmlMessage,"active")=="false"){
                active=0;
            }else{
                active=-1;
            }
        }
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            timestamp = getSafeXmlProperty(xmlMessage, "Timestamp");

            if (timestamp == "false") {

                timestamp = getSafeXmlProperty(xmlMessage, "TimeStamp");

                if (timestamp == "false") {

                    System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
                    allGood = false;
                }
            }
        }

        if (allGood) {

            userObject = new User(0, entityVersion, active, timestamp, uuid, lastName, firstName, phoneNumber, email, street, houseNr, city, postalCode, country, company, userType);

            return userObject;
        } else {
            return null;
        }

    }


    // Session: (params) => XML
    static String getXmlForNewSession(String xmlHeaderDescription, SourceType Source_type, String sessionUUID, String eventUUID, String sessionName, int maxAttendees, String description, String summary, String location, String speaker, String dateTimeStart, String dateTimeEnd, String type, float price, int entityVersion, int active) throws JAXBException {
        String messageType = "sessionMessage";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.SessionStructure sessionStructure = new XmlMessage.SessionStructure(sessionUUID, eventUUID, sessionName, maxAttendees, description, summary, location, dateTimeStart, dateTimeEnd, speaker, type, price, entityVersion, active, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (SessionStructure) in message klasse

        XmlMessage.SessionMessage sessionMessage = new XmlMessage.SessionMessage(header, sessionStructure);
        String xmlTotalMessage = sessionMessage.generateXML();
        return xmlTotalMessage;
    }

    // Session: Object => XML
    static String getXmlFromSessionObject(String headerDescription, SourceType Source_type, Session newSession) throws JAXBException {

        //SourceType Source_type = Source_type;
        String sessionUUID = newSession.getSessionUUID();
        String eventUUID = newSession.getEventUUID();
        String sessionName = newSession.getSessionName();
        int maxAttendees = newSession.getMaxAttendees();
        String sessionDescription = newSession.getDescription();
        String summary = newSession.getSummary();
        String location = newSession.getLocation();
        String contactPerson = newSession.getSpeaker();
        String sessionType = newSession.getType();
        float price = newSession.getPrice();
        int entityVersion = newSession.getEntityVersion();
        int active = newSession.getActive();
        String dateTimeStart = newSession.getDateTimeStart();
        String dateTimeEnd = newSession.getDateTimeEnd();

        String messageType = "sessionMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, headerDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.SessionStructure eventStructure = new XmlMessage.SessionStructure(sessionUUID, eventUUID, sessionName, maxAttendees, sessionDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, sessionType, price, entityVersion, active, Helper.getCurrentDateTimeStamp());
        XmlMessage.SessionMessage xmlReservationMessage = new XmlMessage.SessionMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }

    // Session: XML => Object
    static Session getSessionObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Session sessionObject = null;

        String sessionUUID = "false";
        String eventUUID = "false";
        String sessionName = "false";
        int maxAttendees = 0;
        String description = "false";
        String summary = "false";
        String location = "false";
        String speaker = "false";
        String dateTimeStart = "false";
        String dateTimeEnd = "false";
        String sessionType = "false";
        float price = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        sessionUUID = getSafeXmlProperty(xmlMessage, "uuid");

        if (sessionUUID == "false") {

            sessionUUID = getSafeXmlProperty(xmlMessage, "UUID");

            if (sessionUUID == "false") {

                sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUUID");

                if (sessionUUID == "false") {
                    System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
                    allGood = false;
                }
            }
        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }
        sessionName = getSafeXmlProperty(xmlMessage, "sessionName");
        if (sessionName == "false") {

            System.out.println(" [!!!] ERROR: No sessionName found in XML: ");
            allGood = false;

        }

        try {
            maxAttendees = Integer.parseInt(getSafeXmlProperty(xmlMessage, "maxAttendees"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR(intc): No maxAttendees found in XML: ");
            allGood = false;
        }

        if (maxAttendees < 0) {

            System.out.println(" [!!!] ERROR: No maxAttendees found in XML: ");
            allGood = false;

        }
        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {

            System.out.println(" [!!!] ERROR: No description found in XML: ");
            allGood = false;

        }
        summary = getSafeXmlProperty(xmlMessage, "summary");
        if (summary == "false") {

            System.out.println(" [!!!] ERROR: No summary found in XML: ");
            allGood = false;

        }
        location = getSafeXmlProperty(xmlMessage, "location");
        if (location == "false") {

            System.out.println(" [!!!] ERROR: No location found in XML: ");
            allGood = false;

        }
        speaker = getSafeXmlProperty(xmlMessage, "speaker");
        if (speaker == "false") {

            System.out.println(" [!!!] ERROR: No speaker found in XML: ");
            allGood = false;

        }
        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            allGood = false;

        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            allGood = false;

        }
        sessionType = getSafeXmlProperty(xmlMessage, "sessionType");
        if (sessionType == "false") {

            System.out.println(" [!!!] ERROR: No sessionType found in XML: ");
            allGood = false;

        }
        try {
            price = Float.parseFloat(getSafeXmlProperty(xmlMessage, "price"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            price = -1;
        }
        if (price < 0) {

            System.out.println(" [!!!] ERROR: No price found in XML: ");
            allGood = false;

        }
        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            price = -1;
        }
        if (entityVersion < 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }

        try {
            active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        } catch (NumberFormatException e) {
            //e.printStackTrace();

            //catch boolean instead of int
            if(getSafeXmlProperty(xmlMessage, "active")=="true"){
                active=1;
            }else if(getSafeXmlProperty(xmlMessage,"active")=="false"){
                active=0;
            }else{
                active=-1;
            }
        }
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            timestamp = getSafeXmlProperty(xmlMessage, "Timestamp");

            if (timestamp == "false") {

                timestamp = getSafeXmlProperty(xmlMessage, "TimeStamp");

                if (timestamp == "false") {

                    System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
                    allGood = false;
                }
            }
        }

        sessionObject = new Session(0, entityVersion, active, timestamp, sessionUUID, eventUUID, sessionName, maxAttendees, description, summary, location, speaker, dateTimeStart, dateTimeEnd, sessionType, price);
        return sessionObject;
    }


    // Event: (params) => XML
    static String getXmlForNewEvent(String messageType, String description, SourceType Source_type, String eventUUID, String eventName, int maxAttendees, String eventDescription, String summary,
                                    String location, String contactPerson, String eventType, float price, int entityVersion, int active, String dateTimeStart, String dateTimeEnd) throws JAXBException {

        messageType = "eventMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, description + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.EventStructure eventStructure = new XmlMessage.EventStructure(eventUUID, eventName, maxAttendees, eventDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, entityVersion, active, Helper.getCurrentDateTimeStamp());
        XmlMessage.EventMessage xmlReservationMessage = new XmlMessage.EventMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }

    // Event: Object => XML
    static String getXmlFromEventObject(String headerDescription, SourceType Source_type, Event newEvent) throws JAXBException {

        //SourceType Source_type = Source_type;
        String eventUUID = newEvent.getEventUUID();
        String eventName = newEvent.getEventName();
        int maxAttendees = newEvent.getMaxAttendees();
        String eventDescription = newEvent.getDescription();
        String summary = newEvent.getSummary();
        String location = newEvent.getLocation();
        String contactPerson = newEvent.getContactPerson();
        String eventType = newEvent.getType();
        float price = newEvent.getPrice();
        int entityVersion = newEvent.getEntityVersion();
        int active = newEvent.getActive();
        String dateTimeStart = newEvent.getDateTimeStart();
        String dateTimeEnd = newEvent.getDateTimeEnd();

        String messageType = "eventMessage";
        XmlMessage.Header header = new XmlMessage.Header(messageType, headerDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        XmlMessage.EventStructure eventStructure = new XmlMessage.EventStructure(eventUUID, eventName, maxAttendees, eventDescription, summary, location, contactPerson, dateTimeStart, dateTimeEnd, eventType, price, entityVersion, active, Helper.getCurrentDateTimeStamp());
        XmlMessage.EventMessage xmlReservationMessage = new XmlMessage.EventMessage(header, eventStructure);
        String xmlTotalMessage = xmlReservationMessage.generateXML();
        return xmlTotalMessage;
    }

    // Event: XML => Object
    static Event getEventObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Event eventObject = null;

        String uuid = "false";
        String eventName = "false";
        int maxAttendees = 0;
        String description = "false";
        String summary = "false";
        String location = "false";
        String contactPerson = "false";
        String dateTimeStart = "false";
        String dateTimeEnd = "false";
        String type = "false";
        float price = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        uuid = getSafeXmlProperty(xmlMessage, "uuid");
        if (uuid == "false") {

            uuid = getSafeXmlProperty(xmlMessage, "eventUuid");
            if (uuid == "false") {
                System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
                allGood = false;
            }
        }


        eventName = getSafeXmlProperty(xmlMessage, "eventName");
        if (eventName == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }

        try {
            maxAttendees = Integer.parseInt(getSafeXmlProperty(xmlMessage, "maxAttendees"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: Something went wrong parsing maxAttendees found in XML: ");
        }

        if (maxAttendees < 0) {

            System.out.println(" [!!!] ERROR: No maxAttendees found in XML: ");
            allGood = false;

        }
        description = getSafeXmlProperty(xmlMessage, "description");
        if (description == "false") {

            System.out.println(" [!!!] ERROR: No description found in XML: ");
            allGood = false;

        }
        summary = getSafeXmlProperty(xmlMessage, "summary");
        if (summary == "false") {

            System.out.println(" [!!!] ERROR: No summary found in XML: ");
            allGood = false;

        }
        location = getSafeXmlProperty(xmlMessage, "location");
        if (location == "false") {

            System.out.println(" [!!!] ERROR: No location found in XML: ");
            allGood = false;

        }
        contactPerson = getSafeXmlProperty(xmlMessage, "contactPerson");
        if (contactPerson == "false") {

            System.out.println(" [!!!] ERROR: No contactPerson found in XML: ");
            allGood = false;

        }
        dateTimeStart = getSafeXmlProperty(xmlMessage, "dateTimeStart");
        if (dateTimeStart == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeStart found in XML: ");
            allGood = false;

        }
        dateTimeEnd = getSafeXmlProperty(xmlMessage, "dateTimeEnd");
        if (dateTimeEnd == "false") {

            System.out.println(" [!!!] ERROR: No dateTimeEnd found in XML: ");
            allGood = false;

        }
        type = getSafeXmlProperty(xmlMessage, "eventType");

        if (type == "false") {

            System.out.println(" [!!!] ERROR: No type found in XML: ");
            allGood = false;

        }
        try {
            price = Float.parseFloat(getSafeXmlProperty(xmlMessage, "price"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No price found in XML: ");
            allGood = false;
        }
        if (price < 0) {

            System.out.println(" [!!!] ERROR: Negative price found in XML: ");
            allGood = false;

        }
        entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        if (entityVersion <= 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active < 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            allGood = false;

        }

        eventObject = new Event(0, entityVersion, active, timestamp, uuid, eventName, maxAttendees, description, summary, location, contactPerson, dateTimeStart, dateTimeEnd, type, price);
        return eventObject;
    }


    // Reservation_Session: (params) => XML
    static String getXmlForNewReservation_Session(String xmlHeaderDescription, SourceType Source_type, String reservationUUID, String userUUID, String eventUUID, String sessionUUID, float paid, int entityVersion) throws JAXBException {

        String messageType = "reservationMessage";

        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationStructure reservationStructure = new XmlMessage.ReservationStructure(reservationUUID, userUUID, eventUUID, sessionUUID, messageType.split("Message")[0], paid, entityVersion, 1, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationMessage xmlReservationMessage = new XmlMessage.ReservationMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Session: object => XML
    static String getXmlFromReservation_SessionObject(String xmlHeaderDescription, SourceType Source_type, Reservation_Session thisReservationObject) throws JAXBException {

        String messageType = "reservationMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationStructure reservationStructure = new XmlMessage.ReservationStructure(thisReservationObject.getReservationUUID(), thisReservationObject.getUserUUID(), "", thisReservationObject.getSessionUUID(), messageType.split("Message")[0], thisReservationObject.getPaid(), 1, 1, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationMessage xmlReservationMessage = new XmlMessage.ReservationMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Session: XML => Object
    static Reservation_Session getReservation_SessionObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Reservation_Session reservationSessionObject = null;

        String reservationUUID = "false";
        String userUUID = "false";
        String eventUUID = "false";
        String sessionUUID = "false";
        String type = "false";
        float paid = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        reservationUUID = getSafeXmlProperty(xmlMessage, "uuid");
        if (reservationUUID == "false") {

            eventUUID = getSafeXmlProperty(xmlMessage, "reservationUUID");
            if (eventUUID == "false") {
                System.out.println(" [!!!] ERROR: No reservationUUID found in XML: ");
                allGood = false;
            }
        }
        userUUID = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUUID == "false") {

            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
            allGood = false;

        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }
        sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUuid");
        if (sessionUUID == "false") {

            System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
            allGood = false;

        }

        type = getSafeXmlProperty(xmlMessage, "type");
        if (type == "false") {

            System.out.println(" [!!!] ERROR: No type found in XML: ");
            allGood = false;

        }
        try {
            paid = Float.parseFloat(getSafeXmlProperty(xmlMessage, "paid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No paid found in XML: ");
            allGood = false;
        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR(in tc): No entityVersion found in XML: ");
            allGood = false;
        }

        if (entityVersion == 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active == 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            allGood = false;

        }

        reservationSessionObject = new Reservation_Session(0, entityVersion, active, timestamp, reservationUUID, userUUID, sessionUUID, type, paid);

        return reservationSessionObject;
    }


    // Reservation_Event: (params) => XML
    static String getXmlForNewReservation_Event(String xmlHeaderDescription, SourceType Source_type, String reservationUUID, String userUUID, String eventUUID, float paid, int entityVersion) throws JAXBException {

        String messageType = "reservationMessage";

        String sessionUUID = "";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationStructure reservationStructure = new XmlMessage.ReservationStructure(reservationUUID, userUUID, eventUUID, sessionUUID, messageType.split("Message")[0], paid, entityVersion, 1, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationMessage xmlReservationMessage = new XmlMessage.ReservationMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Event: object => XML
    static String getXmlFromReservation_EventObject(String xmlHeaderDescription, SourceType Source_type, Reservation_Event thisReservationObject) throws JAXBException {

        String messageType = "reservationMessage";
        // form xml
        XmlMessage.Header header = new XmlMessage.Header(messageType, xmlHeaderDescription + ", made on " + Helper.getCurrentDateTimeStamp(), Source_type.toString());
        // set datastructure
        XmlMessage.ReservationStructure reservationStructure = new XmlMessage.ReservationStructure(thisReservationObject.getReservationUUID(), thisReservationObject.getUserUUID(), thisReservationObject.getEventUUID(), "", messageType.split("Message")[0], thisReservationObject.getPaid(), thisReservationObject.getEntityVersion(), 1, Helper.getCurrentDateTimeStamp());
        // steek header en datastructure (Reservationstructure) in message klasse
        XmlMessage.ReservationMessage xmlReservationMessage = new XmlMessage.ReservationMessage(header, reservationStructure);
        // genereer uit de huidige data de XML, de footer met bijhorende checksum wordt automatisch gegenereerd (via XmlMessage.Footer Static functie)
        String xmlTotalMessage = xmlReservationMessage.generateXML();

        //System.out.println("xmlTotalMessage: "+xmlTotalMessage);
        return xmlTotalMessage;
    }

    // Reservation_Event: XML => Object
    static Reservation_Event getReservation_EventObjectFromXmlMessage(String xmlMessage) {

        boolean allGood = true;
        Reservation_Event reservationEventObject = null;

        String reservationUUID = "false";
        String userUUID = "false";
        String eventUUID = "false";
        String sessionUUID = "false";
        String type = "false";
        float paid = 0;
        int entityVersion = 0;
        int active = 0;
        String timestamp = "false";

        // xmlMessage parsing

        reservationUUID = getSafeXmlProperty(xmlMessage, "uuid");
        if (reservationUUID == "false") {

            reservationUUID = getSafeXmlProperty(xmlMessage, "reservationUUID");

            if (reservationUUID == "false") {

                eventUUID = getSafeXmlProperty(xmlMessage, "UUID");

                if (eventUUID == "false") {

                    System.out.println(" [!!!] ERROR: No reservationUUID found in XML: ");

                    allGood = false;
                }
            }
        }
        userUUID = getSafeXmlProperty(xmlMessage, "userUuid");
        if (userUUID == "false") {

            System.out.println(" [!!!] ERROR: No userUUID found in XML: ");
            allGood = false;

        }
        eventUUID = getSafeXmlProperty(xmlMessage, "eventUuid");
        if (eventUUID == "false") {

            System.out.println(" [!!!] ERROR: No eventUUID found in XML: ");
            allGood = false;

        }
        /*sessionUUID = getSafeXmlProperty(xmlMessage, "sessionUUID");
        if (sessionUUID == "false") {

            System.out.println(" [!!!] ERROR: No sessionUUID found in XML: ");
            allGood = false;

        }*/

        try {
            paid = Float.parseFloat(getSafeXmlProperty(xmlMessage, "paid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(" [!!!] ERROR: No paid found in XML: ");
            allGood = false;
        }
        type = getSafeXmlProperty(xmlMessage, "type");
        if (type == "false") {

            System.out.println(" [!!!] ERROR: No type found in XML: ");
            allGood = false;

        }

        try {
            entityVersion = Integer.parseInt(getSafeXmlProperty(xmlMessage, "entityVersion"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (entityVersion == 0) {

            System.out.println(" [!!!] ERROR: No entityVersion found in XML: ");
            allGood = false;

        }
        active = Integer.parseInt(getSafeXmlProperty(xmlMessage, "active"));
        if (active == 0) {

            System.out.println(" [!!!] ERROR: No active found in XML: ");
            allGood = false;

        }
        timestamp = getSafeXmlProperty(xmlMessage, "timestamp");
        if (timestamp == "false") {

            System.out.println(" [!!!] ERROR: No timestamp found in XML: ");
            allGood = false;

        }

        reservationEventObject = new Reservation_Event(0, entityVersion, active, timestamp, reservationUUID, userUUID, eventUUID, type, paid);

        return reservationEventObject;
    }


    //pingMessage:
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


    static String getPropertyFromXml(String xml, String property) throws
            ParserConfigurationException, SAXException, IOException, NullPointerException {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(is);


        NodeList nodes = doc.getElementsByTagName(property);

        String thisMessageType = null;
        try {
            thisMessageType = nodes.item(0).getTextContent();
        } catch (DOMException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return thisMessageType;

        //END of getPropertyFromXml();
    }

    static String getSafeXmlProperty(String xml, String property) {
        String value = "";
        try {

            value = getPropertyFromXml(xml, property);
            //System.out.println(" [i] messageType: " + messageType);

        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            //e.printStackTrace();
            //messageType = "ERROR: No "+property+" found in XML: " + e;
            value = "false";
        }
        return value;
    }

    //https://stackoverflow.com/a/8345074
    static String getCurrentDateTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

}