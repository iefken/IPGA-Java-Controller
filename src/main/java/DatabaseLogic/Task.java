package DatabaseLogic;

import java.util.Objects;

public class Task extends BaseEntity{

    private String taskUuid;
    private String eventUuid;
    private String taskName;
    private String description;
    private String dateTimeStart;
    private String dateTimeEnd;
    private String GCAEventId;
    private String GCAEventLink;


    //CONSTRUCTORS
    public Task(int taskId, int entityVersion, int active, String timestamp,
                String taskUUID,String eventUuid, String taskName, String description, String dateTimeStart, String dateTimeEnd) {

        super(taskId, entityVersion, active, timestamp);

        this.taskUuid = taskUUID;
        this.eventUuid = eventUuid;
        this.taskName = taskName;
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.GCAEventId="";
        this.GCAEventLink="";

    }

    public Task(int taskId, int entityVersion, int active, String timestamp,
                String taskUuid, String eventUuid, String description, String dateTimeStart,
                String dateTimeEnd, boolean insertBaseEntity) {

        super(taskId, entityVersion, active, timestamp, insertBaseEntity);

        this.taskUuid = taskUuid;
        this.eventUuid = eventUuid;
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.GCAEventId="";
        this.GCAEventLink="";

    }


    public Task(int taskId, int entityVersion, int active, String timestamp,
                String taskUUID, String eventUuid, String description, String dateTimeStart,
                String dateTimeEnd, String GCAEventId, String GCAEventLink, boolean insertBaseEntity) {

        super(taskId, entityVersion, active, timestamp, insertBaseEntity);

        this.taskUuid = taskUUID;
        this.eventUuid = eventUuid;
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;

        this.GCAEventId = GCAEventId;
        this.GCAEventLink = GCAEventLink;


    }


    //GETTER & SETTERS
    public int getTaskId() { return this.getEntityId();} // get id from inherited class
    public void setTaskId(int taskId) {
        this.setEntityId(taskId);
    }

    public String getEventUuid(){
        return eventUuid;
    }
    public void setEventUuid(String eventUuid){
        this.eventUuid=eventUuid;
    }

    public String getTaskUuid() { return taskUuid; }
    public void setTaskUuid(String taskUuid) { this.taskUuid = taskUuid; }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
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

    //OTHER

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(taskUuid, task.taskUuid) &&
                Objects.equals(eventUuid, task.eventUuid) &&
                Objects.equals(description, task.description) &&
                Objects.equals(dateTimeStart, task.dateTimeStart) &&
                Objects.equals(dateTimeEnd, task.dateTimeEnd) &&
                Objects.equals(GCAEventId, task.GCAEventId) &&
                Objects.equals(GCAEventLink, task.GCAEventLink);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), taskUuid, eventUuid, description, dateTimeStart, dateTimeEnd, GCAEventId, GCAEventLink);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskUuid='" + taskUuid + '\'' +
                ", eventUuid='" + eventUuid + '\'' +
                ", description='" + description + '\'' +
                ", dateTimeStart='" + dateTimeStart + '\'' +
                ", dateTimeEnd='" + dateTimeEnd + '\'' +
                ", GCAEventId='" + GCAEventId + '\'' +
                ", GCAEventLink='" + GCAEventLink + '\'' +
                ", {'" + super.toString() + "'}"+
                '}';
    }
}