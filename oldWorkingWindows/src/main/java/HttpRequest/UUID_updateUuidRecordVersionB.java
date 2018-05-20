package HttpRequest;

import AppLogic.Helper;

public class UUID_updateUuidRecordVersionB {
    private String Uuid;
    private int Entity_version;
    private Helper.SourceType Source;

    public UUID_updateUuidRecordVersionB(String UUID, int Entity_version, Helper.SourceType Source) {

        this.Uuid = UUID;
        this.Entity_version = Entity_version;
        this.Source = Source;
    }

    //    GETTERS & SETTERS

    public String getUuid() {
        return Uuid;
    }
    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public Helper.SourceType getSource() {
        return Source;
    }
    public void setSource(Helper.SourceType source) {
        Source = source;
    }

    public int getEntity_version() {
        return Entity_version;
    }
    public void setEntity_version(int entity_version) {
        Entity_version = entity_version;
    }

    public String toJSONString() {

        String s = "{\"Uuid\":\""+this.getUuid()+ "\","
                + "\"Entity_version\":\""+this.getEntity_version()+ "\","
                + "\"Source\":\""+this.getSource()+ "\""
                + "}";

        return s;
    }
}
