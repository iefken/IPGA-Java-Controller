package DatabaseLogic;

import java.util.Objects;

public class Session extends BaseEntity{

    private int sessionId;
    private String sessionUUID;
    private String eventUUID;
    private String sessionName;
    private int maxAttendees;
    private String dateTimeStart;
    private String dateTimeEnd;
    private String speaker;
    private String local;
    private String type;

    public Session(int SessionId, int Entity_version, String Status, String Timestamp,
                   String SessionUUID, String eventUUID, String SessionName, int maxAttendees, String DateTimeStart, String DateTimeEnd, String Speaker, String Local, String Type) {

        super(SessionId,Entity_version,Status,Timestamp);

        this.sessionId = SessionId;
        this.sessionUUID = SessionUUID;
        this.eventUUID=eventUUID;
        this.sessionName = SessionName;
        this.maxAttendees = maxAttendees;
        this.dateTimeStart = DateTimeStart;
        this.dateTimeEnd = DateTimeEnd;
        this.speaker = Speaker;
        this.local = Local;
        this.type = Type;
    }


    public int getSessionId() {
        return sessionId;
    }
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getEventUUID() {
        return eventUUID;
    }
    public void setEventUUID(String eventUUID) {
        this.eventUUID = eventUUID;
    }

    public String getSessionUUID() {
        return sessionUUID;
    }
    public void setSessionUUID(String Uuid) {
        this.sessionUUID = Uuid;
    }

    public String getSessionName() {
        return sessionName;
    }
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }
    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
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

    public String getSpeaker() {
        return speaker;
    }
    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        if (!super.equals(o)) return false;
        Session session = (Session) o;
        return Objects.equals(getSessionUUID(), session.getSessionUUID()) &&
                Objects.equals(getSessionName(), session.getSessionName()) &&
                Objects.equals(getDateTimeStart(), session.getDateTimeStart()) &&
                Objects.equals(getDateTimeEnd(), session.getDateTimeEnd()) &&
                Objects.equals(getSpeaker(), session.getSpeaker()) &&
                Objects.equals(getLocal(), session.getLocal()) &&
                Objects.equals(getType(), session.getType());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getSessionUUID(), getSessionName(), getDateTimeStart(), getDateTimeEnd(), getSpeaker(), getLocal(), getType());
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionUUID='" + sessionUUID + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", dateTimeStart='" + dateTimeStart + '\'' +
                ", dateTimeEnd='" + dateTimeEnd + '\'' +
                ", speaker='" + speaker + '\'' +
                ", local='" + local + '\'' +
                ", type='" + type + '\'' +
                ", '" + super.toString() + '\'' +
                '}';
    }
}

