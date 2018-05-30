package DatabaseLogic;

import java.util.Objects;

public class EntitiesToAdd {
    private int idEntitiesToAdd;
    private String table;

    enum EntityToAddStatus {NEW,TOUPDATE,TODELETE,UPTODATE}
    private EntityToAddStatus status = EntityToAddStatus.NEW;
    private int entity_version,active;

    public EntitiesToAdd(int idEntitiesToAdd, String table, EntityToAddStatus status)
    {
        this.idEntitiesToAdd=idEntitiesToAdd;
        this.table = table;
        this.status = status;
    }

    public EntitiesToAdd(String table, EntityToAddStatus status)
    {
        try {
            BaseEntity newBaseEntity = new BaseEntity();
            this.idEntitiesToAdd=newBaseEntity.getEntityId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.table = table;
        this.status = status;
    }

    public EntitiesToAdd(int idEntitiesToAdd, String table, EntityToAddStatus status, int entity_version, int active) {
        this.idEntitiesToAdd = idEntitiesToAdd;
        this.table = table;
        this.status = status;
        this.entity_version = entity_version;
        this.active = active;
    }

    public int getEntity_version() {
        return entity_version;
    }
    public void setEntity_version(int entity_version) {
        this.entity_version = entity_version;
    }

    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }

    public int getIdEntitiesToAdd() {
        return idEntitiesToAdd;
    }
    public void setIdEntitiesToAdd(int idEntitiesToAdd) {
        this.idEntitiesToAdd = idEntitiesToAdd;
    }

    public String getTable() {
        return table;
    }
    public void setTable(String table) {
        this.table = table;
    }

    public EntityToAddStatus getStatus() {
        return status;
    }
    public void setStatus(EntityToAddStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntitiesToAdd)) return false;
        EntitiesToAdd that = (EntitiesToAdd) o;
        return getIdEntitiesToAdd() == that.getIdEntitiesToAdd() &&
                entity_version == that.entity_version &&
                active == that.active &&
                Objects.equals(getTable(), that.getTable()) &&
                getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdEntitiesToAdd(), getTable(), getStatus(), entity_version, active);
    }

    @Override
    public String toString() {
        return "EntitiesToAdd{" +
                "idEntitiesToAdd=" + idEntitiesToAdd +
                ", table='" + table + '\'' +
                ", status=" + status +
                ", entity_version=" + entity_version +
                ", active=" + active +
                '}';
    }
}
