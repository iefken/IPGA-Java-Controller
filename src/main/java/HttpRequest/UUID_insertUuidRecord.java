package HttpRequest;/*
import logic.Helper;*/

//import JsonMessage.JSONObject;

import logic.Helper;

public class UUID_insertUuidRecord {
    private int Source_id;
    private String UUID;
    private int Entity_version;

    private Helper.EntityType Entity_type;
    private Helper.SourceType Source;

    public UUID_insertUuidRecord(UUID_createUuidRecord thisUUID_createUuidRecord, String UUID) {

        this.Source_id = thisUUID_createUuidRecord.getSource_id();
        this.Entity_type = thisUUID_createUuidRecord.getEntity_type();
        this.Source = thisUUID_createUuidRecord.getSource();
        this.Entity_version=1;
        this.UUID=UUID;
    }

    public UUID_insertUuidRecord(int source_id, Helper.EntityType Entity_type, Helper.SourceType thisSourceType, String UUID) {

        this.Source_id = source_id;
        this.Entity_type = Entity_type;
        this.Source = thisSourceType;
        this.Entity_version=1;
        this.UUID=UUID;
    }

    public UUID_insertUuidRecord(int Source_id, Helper.EntityType Entity_type, Helper.SourceType Source, String UUID, int Entity_version) {

        this.Source_id = Source_id;
        this.Entity_type = Entity_type;
        this.Source = Source;
        this.Entity_version=Entity_version;
        this.UUID=UUID;
    }

    //    GETTERS & SETTERS
    public int getSource_id() {
        return Source_id;
    }
    public void setSource_id(int source_id) {
        Source_id = source_id;
    }

    public String getUUID() {
        return UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getEntity_version() {
        return Entity_version;
    }
    public void setEntity_version(int entity_version) {
        Entity_version = entity_version;
    }

    public Helper.EntityType getEntity_type() {
        return Entity_type;
    }
    public void setEntity_type(Helper.EntityType entity_type) {
        Entity_type = entity_type;
    }

    public Helper.SourceType getSource() {
        return Source;
    }
    public void setSource(Helper.SourceType source) {
        Source = source;
    }

//    NEXT?

    @Override
    public String toString() {
        String s = "\n\n[UUID_RESPONSE tostring()]: \n";
        s+= "{\n 'Source_id' : '"+this.getSource_id()+ "' ,\n ";
        s+= " 'UUID' : '"+this.getUUID()+ " ,\n ";
        s+= " 'Entity_version' : '"+this.getEntity_version()+ "' ,\n ";
        s+= " 'Entity_type' : '"+this.getEntity_type()+ "' , \n ";
        s+= " 'Source' : '"+this.getSource()+ "' \n }";

        return s;
    }

    public String toJSONString() {

        String s = "{\"Uuid\":\""+this.getUUID()+ "\","
                + "\"Source_id\":\""+this.getSource_id()+ "\","
                + "\"Entity_type\":\""+this.getEntity_type()+ "\","
                + "\"Entity_version\":\""+this.getEntity_version()+ "\","
                + "\"Source\":\""+this.getSource()+ "\""
                + "}";

        return s;
    }
}
