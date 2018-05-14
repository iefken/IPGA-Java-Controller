package DatabaseLogic;

import AppLogic.Helper;

import java.util.Objects;

public class Event extends BaseEntity{

    private String eventUUID;
    private String eventName;
    private int maxAttendees;
    private String description;
    private String summary;
    private String location;
    private String contactPerson;
    private String dateTimeStart;
    private String dateTimeEnd;
    private String type;
    private String eventLink;
    private float price;

    public Event(int eventId, int entityVersion, int active, String Timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary,
                 String location, String contactPerson,String dateTimeStart, String dateTimeEnd, String type, float price) {

        super(eventId, entityVersion, active, Timestamp);

        this.eventUUID = eventUUID;
        this.eventName = eventName;
        this.maxAttendees = maxAttendees;
        this.description = description;
        this.summary = summary;
        this.location = location;
        this.contactPerson = contactPerson;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;

        this.type = type;
        this.price = price;

        this.eventLink="";

    }

    public Event(int eventId, int entityVersion, int active, String Timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary,
                 String location, String contactPerson,String dateTimeStart, String dateTimeEnd, String type, float price, String eventLink) {

        super(eventId, entityVersion, active, Timestamp);

        this.eventUUID = eventUUID;
        this.eventName = eventName;
        this.maxAttendees = maxAttendees;
        this.description = description;
        this.summary = summary;
        this.location = location;
        this.contactPerson = contactPerson;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;

        this.type = type;
        this.price = price;
        this.eventLink = eventLink;


    }
    //INSERT INTO `PlanningDB`.`BaseEntity` (`entityId`, `entity_version`, `active`, `timestamp`, `timestampLastUpdated`, `timestampCreated`) VALUES (NULL, NULL, NULL, NULL, NULL, NULL);


    public int getEventId() {
        // get id from inherited class
        return this.getEntityId();
    }
    public void setEventId(int eventId) {
        this.setEntityId(eventId);
    }

    public String getEventUUID(){
        return eventUUID;
    }
    public void setEventUUID(String eventUUID){
        this.eventUUID=eventUUID;
    }

    public String getEventname(){
        return eventName;
    }
    public void setEventname(String eventName){
        this.eventName=eventName;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public String getSummary(){
        return summary;
    }
    public void setSummary(String summary){
        this.summary=summary;
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }

    public String getContactperson(){
        return contactPerson;
    }
    public void setContactperson(String contactPerson){
        this.contactPerson=contactPerson;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }
    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public String getContactPerson() {
        return contactPerson;
    }
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDateTimeStart() {
        return dateTimeStart;
    }
    public void setDateTimeStart(String dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public String getDateTimeEnd() {
        return dateTimeEnd;
    }
    public void setDateTimeEnd(String dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    public String getEventLink() {
        return eventLink;
    }
    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }
}