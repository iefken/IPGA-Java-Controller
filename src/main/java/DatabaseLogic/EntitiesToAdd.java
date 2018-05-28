package DatabaseLogic;

import java.sql.SQLException;
import java.util.Objects;

public class EntitiesToAdd {
    private int idEntitiesToAdd;
    private String table;

    enum EntityToAddStatus {NEW,TOUPDATE,TODELETE,UPTODATE}

    private EntityToAddStatus status = EntityToAddStatus.NEW;

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
                Objects.equals(getTable(), that.getTable()) &&
                getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdEntitiesToAdd(), getTable(), getStatus());
    }

    @Override
    public String toString() {
        return "EntitiesToAdd{" +
                "idEntitiesToAdd=" + idEntitiesToAdd +
                ", table='" + table + '\'' +
                ", status=" + status +
                '}';
    }
}
