package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Session_DAO extends BaseDAO {

    //CRUD Statements

    //CREATE

    public int insertIntoSession(Session session) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(session.getEntityId(), session.getEntity_version(), session.getStatus(), session.getTimestamp());

        //execute baseEntity Insert
        int callbackInsertedInt = BaseEntityDAO.insertIntoBaseEntity(newBaseEntity);

        if (session.getEntityId()!=0 && callbackInsertedInt != session.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + session.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        System.out.println("TEST: Given id(" + session.getEntityId() + ") retreived id(" + callbackInsertedInt + ")!");
        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        sqlQuery = "INSERT INTO planningdb.session (`idSession`, `sessionUUID`, `eventUUID`, `sessionName`, `maxAttendees`, `dateTimeStart`, `dateTimeEND`, `speaker`, `local`, `type`) " +
                "VALUES(" + callbackInsertedInt + ",\"" + session.getSessionUUID() + "\",\"" + session.getEventUUID() + "\",\"" + session.getSessionName() + "\",\"" + session.getMaxAttendees() + "\",\"" + session.getDateTimeStart() + "\",\"" + session.getDateTimeEnd() + "\",\"" + session.getSpeaker() + "\",\"" + session.getLocal() + "\",\"" + session.getType() + "\");";

        //INSERT INTO `PlanningDB`.`Session` (`idSession`, `sessionUUID`, `eventUUID`, `sessionName`, `maxAttendees`, `dateTimeStart`, `dateTimeEND`, `speaker`, `local`, `type`) VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
        System.out.println("sqlQuery: "+sqlQuery);

        int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        System.out.println("insertSucces: "+insertSucces+", callbackInsertedInt: "+callbackInsertedInt);
        return insertSucces;

    }

    //READ


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
                sessionReservationsList.add(new Reservation_Session(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }

            return sessionReservationsList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //UPDATE


    //DELETE


    //OTHER


}
