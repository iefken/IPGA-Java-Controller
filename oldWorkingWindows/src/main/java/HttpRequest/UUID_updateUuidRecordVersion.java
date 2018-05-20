package HttpRequest;

//import AppLogic.Helper;

//import AppLogic.Helper;

import AppLogic.Helper;

public class UUID_updateUuidRecordVersion {

    private String Uuid;
    private Helper.SourceType Source;

    public UUID_updateUuidRecordVersion(String UUID, Helper.SourceType Source) {

        this.Uuid = UUID;
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

    public String toJSONString() {

        String s = "{\"Uuid\":\""+this.getUuid()+ "\","
                + "\"Source\":\""+this.getSource()+ "\""
                + "}";

        return s;
    }
}
