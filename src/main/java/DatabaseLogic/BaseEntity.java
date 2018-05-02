package DatabaseLogic;
import Logic.Helper;

import java.util.Objects;

public class BaseEntity {

    private int entityId;
    private int entity_version;
    private String status;
    private String timestamp;

    public BaseEntity(int entityId, int entity_version, String status, String timestamp) {
        this.entityId = entityId;
        this.entity_version = entity_version;
        this.status = status;
        this.timestamp = timestamp;
    }

    public BaseEntity(){}

    public int getEntityId() {
        return entityId;
    }
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntity_version() {
        return entity_version;
    }
    public void setEntity_version(int entity_version) {
        this.entity_version = entity_version;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
                getEntity_version() == that.getEntity_version() &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEntityId(), getEntity_version(), getStatus(), getTimestamp());
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "EntityId=" + entityId +
                ", Entity_version=" + entity_version +
                ", Status='" + status + '\'' +
                ", Timestamp='" + timestamp + '\'' +
                '}';
    }
}
