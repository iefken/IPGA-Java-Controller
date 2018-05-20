package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message")

@XmlType(propOrder = { "uuid","eventName", "maxAttendees", "description", "summary", "location", "contactPerson", "dateTimeStart", "dateTimeEnd", "eventType", "price", "entityVersion", "active", "timestamp" })


public class EventStructure {

    private String uuid;
    private String eventName;
    private int maxAttendees;
    private String description;
    private String summary;
    private String location;
    private String contactPerson;
    private String dateTimeStart;
    private String dateTimeEnd;
    private String eventType;
    private float price;
    private int entityVersion;
    private int active;
    private String timestamp;

    public EventStructure(String eventUUID, String eventName, int maxAttendees, String description, String summary,
                          String location, String contactPerson, String dateTimeStart, String dateTimeEnd, String type, float price,
                          int entityVersion, int active, String timestamp) {


        this.uuid = eventUUID;
        this.eventName = eventName;
        this.maxAttendees = maxAttendees;
        this.description = description;
        this.summary = summary;
        this.location = location;
        this.contactPerson = contactPerson;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.eventType = type;
        this.price = price;
        this.entityVersion = entityVersion;
        this.active = active;
        this.timestamp = timestamp;
    }

    public EventStructure() {

    }


    @XmlElement(name = "uuid")
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String eventUUID) {
        this.uuid = eventUUID;
    }

    @XmlElement(name = "eventName")
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @XmlElement(name = "maxAttendees")
    public int getMaxAttendees() {
        return maxAttendees;
    }
    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "summary")
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @XmlElement(name = "location")
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    @XmlElement(name = "contactPerson")
    public String getContactPerson() {
        return contactPerson;
    }
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @XmlElement(name = "dateTimeStart")
    public String getDateTimeStart() {
        return dateTimeStart;
    }
    public void setDateTimeStart(String dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    @XmlElement(name = "dateTimeEnd")
    public String getDateTimeEnd() {
        return dateTimeEnd;
    }
    public void setDateTimeEnd(String dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    @XmlElement(name = "eventType")
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String type) {
        this.eventType = type;
    }

    @XmlElement(name = "price")
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    @XmlElement(name = "entityVersion")
    public int getEntityVersion() {
        return entityVersion;
    }
    public void setEntityVersion(int entityVersion) {
        this.entityVersion = entityVersion;
    }

    @XmlElement(name = "active")
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }

    @XmlElement(name = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}