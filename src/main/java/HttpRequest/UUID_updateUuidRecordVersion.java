package HttpRequest;

import logic.Helper;

public class UUID_updateUuidRecordVersion {

    private String UUID;
    private Helper.SourceType Source;

    public UUID_updateUuidRecordVersion(String UUID, Helper.SourceType Source) {

        this.UUID = UUID;
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

    public String toJSONString() {

        String s = "{\"Uuid\":\""+this.getUUID()+ "\","
                + "\"Source\":\""+this.getSource()+ "\""
                + "}";

        return s;
    }
}
