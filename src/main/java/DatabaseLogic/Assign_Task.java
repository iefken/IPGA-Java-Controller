package DatabaseLogic;

import java.util.Objects;

public class Assign_Task extends BaseEntity{

    private String assignTaskUuid;
    private String userUuid;
    private String taskUuid;

    //CONSTRUCTOR
    public Assign_Task(int assignTaskId, int entity_version, int active, String timestamp,
                       String assignTaskUuid, String userUuid, String taskUuid) {

        super(assignTaskId,entity_version,active, timestamp);

        this.assignTaskUuid = assignTaskUuid;
        this.userUuid = userUuid;
        this.taskUuid = taskUuid;
    }

    public Assign_Task(int assignTaskId, int entity_version, int active, String timestamp,
                       String assignTaskUuid, String userUuid, String taskUuid, boolean insertBaseEntity) {

        super(assignTaskId,entity_version,active, timestamp,insertBaseEntity);

        this.assignTaskUuid = assignTaskUuid;
        this.userUuid = userUuid;
        this.taskUuid = taskUuid;
    }

    public Assign_Task(int assignTaskId, String assignTaskUuid, String userUuid, String taskUuid) {

        //super(ReservationId,entity_version,Status, Timestamp);

        this.assignTaskUuid = assignTaskUuid;
        this.userUuid = userUuid;
        this.taskUuid = taskUuid;
    }

    //GETTERS & SETTERS
    public int getAssignTaskId() {
        // get id from inherited class
        return this.getEntityId();
    }
    public void setAssignTaskId(int assignTaskId) {
        this.setEntityId(assignTaskId);
    }

    public String getAssignTaskUuid() {
        return assignTaskUuid;
    }
    public void setAssignTaskUuid(String assignTaskUuid) {
        this.assignTaskUuid = assignTaskUuid;
    }

    public String getUserUuid(){
        return userUuid;
    }
    public void setUserUuid(String userUuid){
        this.userUuid=userUuid;
    }

    public String getTaskUuid(){
        return taskUuid;
    }
    public void setTaskUuid(String taskUuid){
        this.taskUuid=taskUuid;
    }

    //OTHER


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assign_Task)) return false;
        if (!super.equals(o)) return false;
        Assign_Task that = (Assign_Task) o;
        return Objects.equals(assignTaskUuid, that.assignTaskUuid) &&
                Objects.equals(userUuid, that.userUuid) &&
                Objects.equals(taskUuid, that.taskUuid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), assignTaskUuid, userUuid, taskUuid);
    }

    @Override
    public String toString() {
        return "Assign_Task{" +
                "assignTaskUuid='" + assignTaskUuid + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", taskUuid='" + taskUuid + '\'' +
                ", {'" + super.toString() + "'}" +
                '}';
    }

}
