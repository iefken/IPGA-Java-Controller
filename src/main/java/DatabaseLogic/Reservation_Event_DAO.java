package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Statement;


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
        //System.out.println("insertSucces: "+insertSucces+", callbackInsertedInt: "+callbackInsertedInt);


        return callbackInsertedInt;

    }

    //READ

//    public BaseEntity getReservationEventByReservationEventId(int thisReservationEventId)
//    {
//        ResultSet rs = null;
//        Event thisReservationEvent = null;
//
//        String sql = "SELECT * FROM User t1 JOIN BaseEntity t2 ON t1.idReservationEvent = t2.entityId WHERE thisReservationEventId = \""+thisReservationEventId+"\";";
//
//        try(Statement s = getConnection().createStatement()){
//
//            if (getConnection().isClosed()) {
//                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
//            }
//
//            rs = s.executeQuery(sql);
//            if(rs.next())
//            {
//
//                thisReservationEvent = new Reservation_Event(rs.getInt(1),rs.getInt(8),rs.getInt(),);
//
//                return thisReservationEvent;
//            }else{
//                throw new IllegalStateException("ERROR 03: No entity found with id: '"+thisReservationEventId+"'...");
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
    /*

    public ArrayList <Reservation_Event> getAllReservation_Events()
    {
        ResultSet rs = null;
        ArrayList<Reservation_Event> eventReservationsList = null;

        String sql = "SELECT * FROM Reservation_Event;";

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            while (rs.next()) {
                eventReservationsList.add(new Reservation_Event(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }

            return eventReservationsList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    public ArrayList <Reservation_Event> getAllReservation_EventsFull()
    {
        ResultSet rs = null;
        ArrayList<Reservation_Event> eventReservationsList = null;

        String sql = "SELECT * FROM Reservation_Event JOIN BaseEntity ON Reservation_Event.reservationId = BaseEntity.entityId;";

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            while (rs.next()) {
                eventReservationsList.add(new Reservation_Event(rs.getInt(1),rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }

            return eventReservationsList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
*/

    //UPDATE


    public int UpdateReservationSession (Reservation_Event newReservationEventFromMessage, int oldEntityId) throws SQLException {

        //Maak een nieuwe BaseEntity met incremented entityVersion
        BaseEntity newBaseEntity = new BaseEntity(newReservationEventFromMessage.getEntityId(), newReservationEventFromMessage.getEntityVersion(), newReservationEventFromMessage.getActive(), newReservationEventFromMessage.getTimestamp());
        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        String sqlQuery = "INSERT INTO PlanningDB.reservation_event (idReservationEvent, reservationUUID, eventUUID, userUUID, paid, timestampLastUpdated, timestampCreated) VALUES (" + callbackInsertedInt + ",\"" + newReservationEventFromMessage.getReservationId() + "\",\"" + newReservationEventFromMessage.getReservationUUID() + "\",\"" + newReservationEventFromMessage.getEventUUID() + "\",\"" + newReservationEventFromMessage.getUserUUID() + "\",\"" + newReservationEventFromMessage.getPaid() + "\",\"" + newReservationEventFromMessage.getTimestamp() + "\");";

        //softdelete oude base entity
        softDeleteBaseEntity("reservation_event", oldEntityId);
        try {
            int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        } catch (Exception e) {
//                e.printStackTrace();
            System.out.println("ERROR inserting reservation_event: " + e);
        }

        return callbackInsertedInt;

    }

    //DELETE



    //OTHER



}
