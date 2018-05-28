package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "message")
@XmlType(propOrder = { "uuid","userUuid", "taskUuid", "entityVersion", "active", "timestamp" })
public class AssignTaskStructure {


	private String uuid;
	private String userUuid;
	private String taskUuid;
	private int entityVersion;
	private int active;
	private String timestamp;

	public AssignTaskStructure(String AssignTaskUuid, String UserUuid, String TaskUuid, int entityVersion, int active, String Timestamp) {

		super();
		this.uuid = AssignTaskUuid;
		this.userUuid = UserUuid;
		this.taskUuid = TaskUuid;
		this.entityVersion = entityVersion;
		this.active = active;
		this.timestamp = Timestamp;
	}

	public AssignTaskStructure() {
		
	}

	@XmlElement(name = "uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String AssignTaskUuid) {
		this.uuid = AssignTaskUuid;
	}

	@XmlElement(name = "userUuid")
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	@XmlElement(name = "taskUuid")
	public String getTaskUuid() {
		return taskUuid;
	}
	public void setTaskUuid(String taskUuid) {
		this.taskUuid = taskUuid;
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
