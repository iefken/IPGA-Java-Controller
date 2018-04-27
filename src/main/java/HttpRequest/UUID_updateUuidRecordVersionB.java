package HttpRequest;

import logic.Helper;

public class UUID_updateUuidRecordVersionB {
    private String UUID;
    private int Entity_version;
    private Helper.SourceType Source;

    public UUID_updateUuidRecordVersionB(String UUID, int Entity_version, Helper.SourceType Source) {

        this.UUID = UUID;
        this.Entity_version = Entity_version;
        this.Source = Source;
    }

    //    GETTERS & SETTERS

    public String getUUID() {
        return UUID;
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

        String s = "{\"Uuid\":\""+this.getUUID()+ "\","
                + "\"Entity_version\":\""+this.getEntity_version()+ "\","
                + "\"Source\":\""+this.getSource()+ "\""
                + "}";

        return s;
    }
}
