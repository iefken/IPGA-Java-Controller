package DatabaseLogic;

import AppLogic.Helper;

import java.sql.SQLException;
import java.util.Objects;

public class BaseEntity {

    private int entityId;
    private int entityVersion;
    private int active;
    private String timestamp;

    public BaseEntity(int entityId, int entityVersion, int active, String timestamp) {

        this.entityVersion = entityVersion;
        this.active = active;
        this.timestamp = timestamp;

        if (entityId == 0) {

            //id=0 => doesn't exist yet => insert in DB and get id

            BaseEntityDAO thisBaseEntityDAO = new BaseEntityDAO();

            try {
                entityId = thisBaseEntityDAO.insertIntoBaseEntity(this);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.entityId = entityId;
    }

    public BaseEntity() {
    }

    public int getEntityId() {
        return entityId;
    }
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityVersion() {
        return entityVersion;
    }
    public void setEntityVersion(int entityVersion) {
        this.entityVersion = entityVersion;
    }

    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return getEntityId() == that.getEntityId() &&
                getEntityVersion() == that.getEntityVersion() &&
                getActive() == that.getActive() &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEntityId(), getEntityVersion(), getActive(), getTimestamp());
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "entityId=" + entityId +
                ", entityVersion=" + entityVersion +
                ", active=" + active +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

