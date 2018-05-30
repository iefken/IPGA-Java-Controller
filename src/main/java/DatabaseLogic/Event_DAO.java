package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Event_DAO extends BaseEntityDAO {

    //CRUD Statements
    //CREATE
    public int insertIntoEvent(Event event) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(event.getEntityId(), event.getEntityVersion(), event.getActive(), event.getTimestamp());

        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (event.getEntityId() != 0 && callbackInsertedInt != event.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + event.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        sqlQuery = "INSERT INTO PlanningDB.Event (`idEvent`, `uuid`, `eventName`, `maxAttendees`, `description`, `summary`, `location`,`contactPerson`,`dateTimeStart`,`dateTimeEnd`, `type`, `price`, `GCAEventId`, `GCAEventLink`) VALUES (" + callbackInsertedInt + ",\"" + event.getEventUUID() + "\",\"" + event.getEventName() + "\",\"" + event.getMaxAttendees() + "\",\"" + event.getDescription() + "\",\"" + event.getSummary() + "\",\"" + event.getLocation() + "\",\"" + event.getContactPerson() + "\",\"" + event.getDateTimeStart() + "\",\"" + event.getDateTimeEnd() + "\",\"" + event.getType() + "\",\"" + event.getPrice() + "\",\"" + event.getGCAEventId() + "\",\"" + event.getGCAEventLink() + "\");";

        //INSERT INTO `PlanningDB`.`Session` (`idSession`, `sessionUUID`, `eventUUID`, `sessionName`, `maxAttendees`, `dateTimeStart`, `dateTimeEND`, `speaker`, `local`, `type`) VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

        int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);

        return callbackInsertedInt;

    }

    //READ


    //UPDATE
    public boolean updateEventByObject (Event newEventFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.Event SET " +

                "eventName=\""+newEventFromMessage.getEventName()+"\", " +
                "maxAttendees="+newEventFromMessage.getMaxAttendees()+", " +
                "description=\""+newEventFromMessage.getDescription()+"\", " +
                "summary=\""+newEventFromMessage.getSummary()+"\", " +
                "location=\""+newEventFromMessage.getLocation()+"\", " +
                "contactPerson=\""+newEventFromMessage.getContactPerson()+"\", " +
                "dateTimeStart=\""+newEventFromMessage.getDateTimeStart()+"\", " +
                "dateTimeEnd=\""+newEventFromMessage.getDateTimeEnd()+"\", " +
                "type=\""+newEventFromMessage.getType()+"\", " +
                "GCAEventId=\"" +newEventFromMessage.getGCAEventId()+"\", " +
                "GCAEventLink=\"" +newEventFromMessage.getGCAEventLink()+"\", " +
                "price="+newEventFromMessage.getPrice()+" " +

                "WHERE uuid=\""+newEventFromMessage.getEventUUID()+"\" " +

                ";";
        boolean allGood = true;

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateEventByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newEventFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newEventFromMessage.getEventId());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating Event with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateEventByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\nInserting Event (ev."+newEventFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
                if(statement != null)

                    statement.close();
            }catch(SQLException e){
                System.out.println(e.getMessage());;
                throw new RuntimeException("ERROR 02: Error during closing the connection...");
            }
        }

    }


    //DELETE


    //OTHER


}
