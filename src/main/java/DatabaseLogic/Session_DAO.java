package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Session_DAO extends BaseEntityDAO {

    //CRUD Statements

    //CREATE

    public int insertIntoSession(Session session) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(session.getEntityId(), session.getEntityVersion(), session.getActive(), session.getTimestamp());

        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (session.getEntityId()!=0 && callbackInsertedInt != session.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + session.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        sqlQuery = "INSERT INTO PlanningDB.Session (`idSession`, `uuid`, `eventUuid`, `sessionName`, `maxAttendees`, `description`, `summary`, `location`, `speaker`, `dateTimeStart`, `dateTimeEnd`, `type`, `GCAEventId`,`GCAEventLink`, `price`) " +
                "VALUES(" + callbackInsertedInt + ",\"" + session.getSessionUUID() + "\",\"" + session.getEventUUID() + "\",\"" + session.getSessionName() + "\"," + session.getMaxAttendees() + ",\"" + session.getDescription() + "\",\"" + session.getSummary() + "\",\"" + session.getLocation() + "\",\"" + session.getSpeaker() + "\",\"" + session.getDateTimeStart() + "\",\"" + session.getDateTimeEnd() + "\",\"" + session.getType() + "\",\"" + session.getGCAEventId() + "\",\"" + session.getGCAEventLink() + "\",\"" + session.getPrice() + "\");";

        //System.out.println("sqlquery: "+sqlQuery);
        try {
            int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        } catch (RuntimeException e) {
            //e.printStackTrace();
            System.out.println("[!!!] ERROR: Running query:\n"+sqlQuery+"\n"+e);
        }

        return callbackInsertedInt;

    }

    //READ

    //UPDATE

    public boolean updateSessionByObject (Session newSessionFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.session SET " +
                "eventUuid=\""+newSessionFromMessage.getEventUUID()+"\", " +
                "sessionName=\""+newSessionFromMessage.getSessionName()+"\", " +
                "maxAttendees="+newSessionFromMessage.getMaxAttendees()+", " +
                "description=\""+newSessionFromMessage.getDescription()+"\", " +
                "summary=\""+newSessionFromMessage.getSummary()+"\", " +
                "location=\""+newSessionFromMessage.getLocation()+"\", " +
                "speaker=\""+newSessionFromMessage.getSpeaker()+"\", " +
                "dateTimeStart=\""+newSessionFromMessage.getDateTimeStart()+"\", " +
                "dateTimeEnd=\""+newSessionFromMessage.getDateTimeEnd()+"\", " +
                "type=\""+newSessionFromMessage.getType()+"\", " +
                "price="+newSessionFromMessage.getPrice()+" " +

                "WHERE uuid=\""+newSessionFromMessage.getSessionUUID()+"\" " +

                ";";
        boolean allGood = true;

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateSessionByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newSessionFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newSessionFromMessage.getSessionId());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating Session with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateSessionByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                System.out.println("Queried:\nSTART\nInserting Session (ev."+newSessionFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
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
