package HttpRequest;

public class UUID_updateUuidRecordVersionResponse {

    private int Entity_version;

    public UUID_updateUuidRecordVersionResponse(int Entity_version) {

        this.Entity_version = Entity_version;
    }

    //    GETTERS & SETTERS

    public int getEntity_version() {
        return Entity_version;
    }
    public void setEntity_version(int Entity_version) {
        this.Entity_version = Entity_version;
    }


    public String toJSONString() {

        String s = "{\"Entity_version\":\""+this.getEntity_version()+ "\""
                + "}";

        return s;
    }
}
