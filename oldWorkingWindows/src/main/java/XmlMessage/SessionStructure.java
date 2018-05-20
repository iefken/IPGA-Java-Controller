package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlRootElement(name = "message")
@XmlType(propOrder = { "uuid", "eventUuid","sessionName", "maxAttendees", "description", "summary","location", "speaker","dateTimeStart", "dateTimeEnd", "sessionType", "price", "entityVersion", "active", "timestamp"})
public class SessionStructure {

	private String uuid;
	private String eventUuid;
	private String sessionName;
	private int maxAttendees;
	private String description;
	private String summary;
	private String location;
	private String speaker;
	private String dateTimeStart;
	private String dateTimeEnd;
	private String sessionType;
	private float price;
	private int entityVersion;
	private int active;
	private String timestamp;

	public SessionStructure(String sessionUUID,String eventUUID, String sessionName, int maxAttendees, String description, String summary, String location, String speaker, String dateTimeStart, String dateTimeEnd, String type, float price, int entityVersion, int active, String timestamp) {
		super();
		this.uuid = sessionUUID;
		this.eventUuid = eventUUID;
		this.sessionName = sessionName;
		this.maxAttendees = maxAttendees;
		this.description = description;
		this.summary = summary;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeEnd = dateTimeEnd;
		this.speaker = speaker;
		this.location = location;
		this.sessionType = type;
		this.price = price;
		this.entityVersion = entityVersion;
		this.active = active;
		this.timestamp = timestamp;
	}

	public SessionStructure() {
		
	}

	@XmlElement(name = "uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String sessionUUID) {
		this.uuid = sessionUUID;
	}

	@XmlElement(name = "eventUuid")
	public String getEventUuid() {
		return eventUuid;
	}
	public void setEventUuid(String eventUUID) {
		this.eventUuid = eventUUID;
	}

	@XmlElement(name = "sessionName")
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
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

	@XmlElement(name = "speaker")
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
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

	@XmlElement(name = "sessionType")
	public String getSessionType() {
		return sessionType;
	}
	public void setSessionType(String type) {
		this.sessionType = type;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SessionStructure)) return false;
		SessionStructure that = (SessionStructure) o;
		return getMaxAttendees() == that.getMaxAttendees() &&
				Float.compare(that.getPrice(), getPrice()) == 0 &&
				getEntityVersion() == that.getEntityVersion() &&
				getActive() == that.getActive() &&
				Objects.equals(getUuid(), that.getUuid()) &&
				Objects.equals(getEventUuid(), that.getEventUuid()) &&
				Objects.equals(getSessionName(), that.getSessionName()) &&
				Objects.equals(getDescription(), that.getDescription()) &&
				Objects.equals(getSummary(), that.getSummary()) &&
				Objects.equals(getLocation(), that.getLocation()) &&
				Objects.equals(getSpeaker(), that.getSpeaker()) &&
				Objects.equals(getDateTimeStart(), that.getDateTimeStart()) &&
				Objects.equals(getDateTimeEnd(), that.getDateTimeEnd()) &&
				Objects.equals(getSessionType(), that.getSessionType()) &&
				Objects.equals(getTimestamp(), that.getTimestamp());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getUuid(), getEventUuid(), getSessionName(), getMaxAttendees(), getDescription(), getSummary(), getLocation(), getSpeaker(), getDateTimeStart(), getDateTimeEnd(), getSessionType(), getPrice(), getEntityVersion(), getActive(), getTimestamp());
	}

	@Override
	public String toString() {
		return "SessionStructure{" +
				"sessionUUID='" + uuid + '\'' +
				", eventUUID='" + eventUuid + '\'' +
				", sessionName='" + sessionName + '\'' +
				", maxAttendees=" + maxAttendees +
				", description='" + description + '\'' +
				", summary='" + summary + '\'' +
				", location='" + location + '\'' +
				", speaker='" + speaker + '\'' +
				", dateTimeStart='" + dateTimeStart + '\'' +
				", dateTimeEnd='" + dateTimeEnd + '\'' +
				", type='" + sessionType + '\'' +
				", price=" + price +
				", entityVersion=" + entityVersion +
				", active=" + active +
				", timestamp='" + timestamp + '\'' +
				'}';
	}
}

