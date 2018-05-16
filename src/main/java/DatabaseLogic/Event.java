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
    private String GCAEventId;
    private String GCAEventLink;
    private float price;

    public Event(int eventId, int entityVersion, int active, String timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary,
                 String location, String contactPerson,String dateTimeStart, String dateTimeEnd, String type, float price) {

        super(eventId, entityVersion, active, timestamp);

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

        this.GCAEventId="";
        this.GCAEventLink="";

    }

    public Event(int eventId, int entityVersion, int active, String timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary,
                 String location, String contactPerson,String dateTimeStart, String dateTimeEnd, String type, float price, boolean insertBaseEntity) {

        super(eventId, entityVersion, active, timestamp, insertBaseEntity);

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

        this.GCAEventId="";
        this.GCAEventLink="";

    }
    public Event(int eventId, int entityVersion, int active, String Timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary,
                 String location, String contactPerson,String dateTimeStart, String dateTimeEnd, String type, float price, String GCAEventId, String GCAEventLink) {

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

        this.GCAEventId = GCAEventId;
        this.GCAEventLink = GCAEventLink;


    }

    public Event(int eventId, int entityVersion, int active, String timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary,
                 String location, String contactPerson,String dateTimeStart, String dateTimeEnd, String type, float price, String GCAEventId, String GCAEventLink, boolean insertBaseEntity) {

        super(eventId, entityVersion, active, timestamp, insertBaseEntity);

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

        this.GCAEventId = GCAEventId;
        this.GCAEventLink = GCAEventLink;


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

    public String getGCAEventId() {
        return GCAEventId;
    }
    public void setGCAEventId(String GCAEventId) {
        this.GCAEventId = GCAEventId;
    }

    public String getGCAEventLink() {
        return GCAEventLink;
    }
    public void setGCAEventLink(String eventLink) {
        this.GCAEventLink = eventLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        if (!super.equals(o)) return false;
        Event event = (Event) o;
        return getMaxAttendees() == event.getMaxAttendees() &&
                Float.compare(event.getPrice(), getPrice()) == 0 &&
                Objects.equals(getEventUUID(), event.getEventUUID()) &&
                Objects.equals(getEventName(), event.getEventName()) &&
                Objects.equals(getDescription(), event.getDescription()) &&
                Objects.equals(getSummary(), event.getSummary()) &&
                Objects.equals(getLocation(), event.getLocation()) &&
                Objects.equals(getContactPerson(), event.getContactPerson()) &&
                Objects.equals(getDateTimeStart(), event.getDateTimeStart()) &&
                Objects.equals(getDateTimeEnd(), event.getDateTimeEnd()) &&
                Objects.equals(getType(), event.getType()) &&
                Objects.equals(getGCAEventId(), event.getGCAEventId()) &&
                Objects.equals(getGCAEventLink(), event.getGCAEventLink());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getEventUUID(), getEventName(), getMaxAttendees(), getDescription(), getSummary(), getLocation(), getContactPerson(), getDateTimeStart(), getDateTimeEnd(), getType(), getGCAEventId(), getGCAEventLink(), getPrice());
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventUUID='" + eventUUID + '\'' +
                ", eventName='" + eventName + '\'' +
                ", maxAttendees=" + maxAttendees +
                ", description='" + description + '\'' +
                ", summary='" + summary + '\'' +
                ", location='" + location + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", dateTimeStart='" + dateTimeStart + '\'' +
                ", dateTimeEnd='" + dateTimeEnd + '\'' +
                ", type='" + type + '\'' +
                ", GCAEventId='" + GCAEventId + '\'' +
                ", GCAEventLink='" + GCAEventLink + '\'' +
                ", price=" + price +
                ", {'" + super.toString() + "'}" +
                '}';
    }
}