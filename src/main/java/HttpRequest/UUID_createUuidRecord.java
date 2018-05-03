package HttpRequest;

import JsonMessage.JSONObject;
import AppLogic.Helper;

public class UUID_createUuidRecord extends JSONObject {

    private int Source_id;
    private Helper.EntityType Entity_type;
    private Helper.SourceType Source;

    public UUID_createUuidRecord(int Source_id, Helper.EntityType Entity_type, Helper.SourceType Source) {
        this.Source_id = Source_id;
        this.Entity_type = Entity_type;
        this.Source = Source;

    }

    //    GETTERS & SETTERS
    public int getSource_id() {
        return Source_id;
    }
    public void setSource_id(int source_id) {
        Source_id = source_id;
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

        String s = "\n\n[UUID_REQUEST tostring()]: \n";
        s+= "{\n 'Source_id' : '"+this.getSource_id()+ "' ,\n ";
        s+= " 'Entity_type' : '"+this.getEntity_type()+ " , \n ";
        s+= " 'Source' : '"+this.getSource()+ " \n }";

        return s;
    }

    public String toJSONString() {

        String s = "{\"Source_id\":\""+this.getSource_id()+ "\","
                + "\"Entity_type\":\""+this.getEntity_type()+ "\","
                + "\"Source\":\""+this.getSource()+ "\""
                + "}";
        return s;
    }

}
