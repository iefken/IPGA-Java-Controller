package DatabaseLogic;

import java.sql.SQLException;
import java.util.Objects;

public class BaseEntity {

    private int idBaseEntity;
    private int entityVersion;
    private int active;
    private String timestamp;

    public BaseEntity(int entityId, int entityVersion, int active, String timestamp, boolean insertBaseEntity) {

        this.entityVersion = entityVersion;
        this.active = active;
        this.timestamp = timestamp;
        this.idBaseEntity = entityId;
    }

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
                System.out.println("SQL ERROR during BaseEntity Insertion: "+e);
                //e.printStackTrace();
            }
        }
        this.idBaseEntity = entityId;
    }
    public BaseEntity() {
    }

    public int getEntityId() {
        return idBaseEntity;
    }
    public void setEntityId(int entityId) {
        this.idBaseEntity = entityId;
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
        return idBaseEntity == that.idBaseEntity &&
                getEntityVersion() == that.getEntityVersion() &&
                getActive() == that.getActive() &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {

        return Objects.hash(idBaseEntity, getEntityVersion(), getActive(), getTimestamp());
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "idBaseEntity=" + idBaseEntity +
                ", entityVersion=" + entityVersion +
                ", active=" + active +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

