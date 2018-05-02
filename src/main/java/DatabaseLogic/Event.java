package DatabaseLogic;

import java.util.Objects;

public class Event extends BaseEntity{

    private String eventUUID;
    private String eventName;
    private int maxAttendees;
    private String description;
    private String summary;
    private String location;
    private String contactPerson;

    public Event(int eventId, int entity_version, String status, String Timestamp,
                 String eventUUID, String eventName, int maxAttendees, String description, String summary, String location, String contactPerson) {

        super(eventId, entity_version,status,Timestamp);

        this.eventUUID = eventUUID;
        this.eventName = eventName;
        this.maxAttendees = maxAttendees;
        this.description = description;
        this.summary = summary;
        this.location = location;
        this.contactPerson = contactPerson;
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

    public int getMaxattendees(){
        return maxAttendees;
    }
    public void setMaxattendees(int maxAttendees){
        this.maxAttendees=maxAttendees;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        if (!super.equals(o)) return false;
        Event event = (Event) o;
        return maxAttendees == event.maxAttendees &&
                Objects.equals(getEventUUID(), event.getEventUUID()) &&
                Objects.equals(eventName, event.eventName) &&
                Objects.equals(getDescription(), event.getDescription()) &&
                Objects.equals(getSummary(), event.getSummary()) &&
                Objects.equals(getLocation(), event.getLocation()) &&
                Objects.equals(contactPerson, event.contactPerson);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getEventUUID(), eventName, maxAttendees, getDescription(), getSummary(), getLocation(), contactPerson);
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
                ", '" + super.toString() + '\'' +
                '}';
    }
}