package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Reservation_Event_DAO extends BaseEntityDAO{

    //CRUD Statements
    //CREATE
    public int insertIntoReservation_Event(Reservation_Event reservation_event) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(reservation_event.getEntityId(),reservation_event.getEntityVersion(),reservation_event.getActive(),reservation_event.getTimestamp());

        int callbackInsertedInt = newBaseEntity.getEntityId();

        if(reservation_event.getEntityId()!=0 && callbackInsertedInt != reservation_event.getEntityId())
        {
            throw new SQLException("ERROR 05: Given id("+reservation_event.getEntityId()+") does not correspond to retreived id("+callbackInsertedInt+")!");
        }

        PreparedStatement preparedStatement = null;

        String sqlQuery ="";

        if(reservation_event.getEntityId()==0)
        {
            sqlQuery = "INSERT INTO PlanningDB.Reservation_Event (`uuid`, `eventUuid`, `userUuid`) VALUES(\""+reservation_event.getReservationUUID()+"\",\""+reservation_event.getEventUUID()+"\",\""+reservation_event.getUserUUID()+"\");";
        }else{
            sqlQuery = "INSERT INTO PlanningDB.Reservation_Event (`idReservationEvent`, `uuid`, `eventUuid`, `userUuid`) VALUES(\""+callbackInsertedInt+"\",\""+reservation_event.getReservationUUID()+"\",\""+reservation_event.getEventUUID()+"\",\""+reservation_event.getUserUUID()+"\");";
        }

        //System.out.println("sqlQuery= "+sqlQuery);

        int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);

        return callbackInsertedInt;

    }

    //READ

    //UPDATE
    public boolean updateReservationEventByObject (Reservation_Event newReservationEventFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.Reservation_Event SET " +
                "eventUuid=\""+newReservationEventFromMessage.getEventUUID()+"\", " +
                "userUuid="+newReservationEventFromMessage.getUserUUID()+", " +
                "paid=\""+newReservationEventFromMessage.getPaid()+"\" " +

                "WHERE uuid=\""+newReservationEventFromMessage.getEventUUID()+"\" " +
                ";";
        boolean allGood = true;

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateReservationEventByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newReservationEventFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newReservationEventFromMessage.getReservationId());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating reservation  event  with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateReservationEventByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\nInserting reservation event (ev."+newReservationEventFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
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
