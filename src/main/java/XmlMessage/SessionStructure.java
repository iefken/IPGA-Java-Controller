package XmlMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "basemessage")
@XmlType(propOrder = { "uuid","sessionName", "dateTimeStart", "dateTimeEnd", "speaker", "local", "type", "status", "timestamp"})
public class SessionStructure {

	private String Uuid;
	private String sessionName;
	private String dateTimeStart;
	private String dateTimeEnd;
	private String speaker;
	private String local;
	private String type;
	private int status;
	private String timestamp;

	public SessionStructure(String Uuid, String sessionName, String dateTimeStart, String dateTimeEnd, String speaker, String local, String type, int status, String timestamp) {
		super();
		this.Uuid = Uuid;
		this.sessionName = sessionName;
		this.dateTimeStart = dateTimeStart;
		this.dateTimeEnd = dateTimeEnd;
		this.speaker = speaker;
		this.local = local;
		this.type = type;
		this.status = status;
		this.timestamp = timestamp;
	}

	public SessionStructure() {
		
	}


	@XmlElement(name = "uuid")
	public String getUuid() {
		return Uuid;
	}
	public void setUuid(String Uuid) {
		this.Uuid = Uuid;
	}

	@XmlElement(name = "sessionName")
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
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

	@XmlElement(name = "speaker")
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	@XmlElement(name = "local")
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
