package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Reservation_Session_DAO extends BaseEntityDAO {

    //CRUD Statements

    //CREATE

    public int insertIntoReservation_Session(Reservation_Session reservation_session) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(reservation_session.getEntityId(), reservation_session.getEntityVersion(), reservation_session.getActive(), reservation_session.getTimestamp());

        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (reservation_session.getEntityId()!=0 && callbackInsertedInt != reservation_session.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + reservation_session.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        //System.out.println("TEST: Given id(" + reservation_session.getEntityId() + ") retreived id(" + callbackInsertedInt + ")!");
        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        if(reservation_session.getEntityId()==0) {
            sqlQuery = "INSERT INTO PlanningDB.Reservation_Event (`uuid`, `eventUuid`, `userUuid`) VALUES(\"" + reservation_session.getEntityId() + "\",\"" + reservation_session.getSessionUUID() + "\",\"" + reservation_session.getUserUUID() + "\");";
        }else{
            sqlQuery = "INSERT INTO PlanningDB.Reservation_Session (`idReservationSession`, `uuid`, `sessionUuid`, `userUuid`) VALUES(" + callbackInsertedInt + ",\"" + reservation_session.getReservationUUID() + "\",\"" + reservation_session.getSessionUUID() + "\",\"" + reservation_session.getUserUUID() + "\");";
        }
        //sqlQuery = "INSERT INTO PlanningDB.Reservation_Session (`reservationId`, `reservationUUID`, `sessionUUID`, `userUUID`) VALUES(\"" + callbackInsertedInt + "\",\"" + reservation_session.getReservationUUID() + "\",\"" + reservation_session.getSessionUUID() + "\",\"" + reservation_session.getUserUUID() + "\");";
        //INSERT INTO `PlanningDB`.`Reservation_Session` (`idReservation`, `reservationUUID`, `sessionUUID`, `userUUID`) VALUES (NULL, NULL, NULL, NULL);

        //System.out.println("sqlQuery: "+sqlQuery);

        int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        //System.out.println("insertSucces: "+insertSucces+", callbackInsertedInt: "+callbackInsertedInt);
        return callbackInsertedInt;

    }

    //READ

/*

    public ArrayList<Reservation_Session> getAllReservation_Sessions() {
        ResultSet rs = null;
        ArrayList<Reservation_Session> sessionReservationsList = null;

        String sql = "SELECT * FROM Reservation_Session;";

        try (Statement s = getConnection().createStatement()) {

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            while (rs.next()) {
                sessionReservationsList.add(new Reservation_Session(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }

            return sessionReservationsList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArrayList<Reservation_Session> getAllReservation_SessionsFull() {
        ResultSet rs = null;
        ArrayList<Reservation_Session> sessionReservationsList = null;

        String sql = "SELECT * FROM Reservation_Session JOIN BaseEntity ON Reservation_Session.reservationId = BaseEntity.entityId;";

        try (Statement s = getConnection().createStatement()) {

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            while (rs.next()) {
                sessionReservationsList.add(new Reservation_Session(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }

            return sessionReservationsList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
*/

    //UPDATE

    public int UpdateReservationSession (Reservation_Session newReservationSessionFromMessage, int oldEntityId) throws SQLException {

        //Maak een nieuwe BaseEntity met incremented entityVersion
        BaseEntity newBaseEntity = new BaseEntity(newReservationSessionFromMessage.getEntityId(), newReservationSessionFromMessage.getEntityVersion(), newReservationSessionFromMessage.getActive(), newReservationSessionFromMessage.getTimestamp());
        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        String sqlQuery = "INSERT INTO PlanningDB.reservation_session (idReservationSession, reservationUUID, sessionUUID, userUUID, paid, timestampLastUpdated, timestampCreated) VALUES (" + callbackInsertedInt + ",\"" + newReservationSessionFromMessage.getReservationId() + "\",\"" + newReservationSessionFromMessage.getReservationUUID() + "\",\"" + newReservationSessionFromMessage.getSessionUUID() + "\",\"" + newReservationSessionFromMessage.getUserUUID() + "\",\"" + newReservationSessionFromMessage.getPaid() + "\",\"" + newReservationSessionFromMessage.getTimestamp() + "\");";

        //softdelete oude base entity
        softDeleteBaseEntity("reservation_session", oldEntityId);
        try {
            int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        } catch (Exception e) {
//                e.printStackTrace();
            System.out.println("ERROR inserting reservation_session: " + e);
        }

        return callbackInsertedInt;

    }

    //DELETE


    //OTHER


}
