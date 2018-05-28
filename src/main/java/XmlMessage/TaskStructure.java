package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlRootElement(name = "message")
@XmlType(propOrder = { "uuid", "eventUuid", "taskName", "description", "dateTimeStart", "dateTimeEnd", "entityVersion", "active", "timestamp"})
public class TaskStructure {

	private String uuid;
	private String eventUuid;
	private String taskName;
	private String description;
	private String dateTimeStart;
	private String dateTimeEnd;
	private int entityVersion;
	private int active;
	private String timestamp;

	public TaskStructure(String taskUuid, String eventUUID, String taskName, String description, String dateTimeStart, String dateTimeEnd, int entityVersion, int active, String timestamp) {
		super();
		this.uuid = taskUuid;
		this.eventUuid = eventUUID;
		this.taskName = taskName;
		this.description = description;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeEnd = dateTimeEnd;
		this.entityVersion = entityVersion;
		this.active = active;
		this.timestamp = timestamp;
	}

	public TaskStructure() {
		
	}

	@XmlElement(name = "uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String taskUuid) {
		this.uuid = taskUuid;
	}

	@XmlElement(name = "eventUuid")
	public String getEventUuid() {
		return eventUuid;
	}
	public void setEventUuid(String eventUUID) {
		this.eventUuid = eventUUID;
	}

	@XmlElement(name = "taskName")
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@XmlElement(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TaskStructure)) return false;
		TaskStructure that = (TaskStructure) o;
		return getEntityVersion() == that.getEntityVersion() &&
				getActive() == that.getActive() &&
				Objects.equals(getUuid(), that.getUuid()) &&
				Objects.equals(getEventUuid(), that.getEventUuid()) &&
				Objects.equals(getDescription(), that.getDescription()) &&
				Objects.equals(getDateTimeStart(), that.getDateTimeStart()) &&
				Objects.equals(getDateTimeEnd(), that.getDateTimeEnd()) &&
				Objects.equals(getTimestamp(), that.getTimestamp());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getUuid(), getEventUuid(), getDescription(), getDateTimeStart(), getDateTimeEnd(), getEntityVersion(), getActive(), getTimestamp());
	}

	@Override
	public String toString() {
		return "SessionStructure{" +
				"sessionUUID='" + uuid + '\'' +
				", eventUUID='" + eventUuid + '\'' +
				", description='" + description + '\'' +
				", dateTimeStart='" + dateTimeStart + '\'' +
				", dateTimeEnd='" + dateTimeEnd + '\'' +
				", entityVersion=" + entityVersion +
				", active=" + active +
				", timestamp='" + timestamp + '\'' +
				'}';
	}
}

