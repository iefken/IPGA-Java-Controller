package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Reservation_Session_DAO extends BaseEntityDAO {

    //CRUD Statements
    //CREATE
    public int insertIntoReservation_Session(Reservation_Session reservation_session) throws SQLException {

        //execute baseEntity Insert
        BaseEntity newBaseEntity = new BaseEntity(reservation_session.getEntityId(), reservation_session.getEntityVersion(), reservation_session.getActive(), reservation_session.getTimestamp());

        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (reservation_session.getEntityId()!=0 && callbackInsertedInt != reservation_session.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + reservation_session.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        //System.out.println("TEST: Given id(" + reservation_session.getEntityId() + ") retreived id(" + callbackInsertedInt + ")!");
        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        if(reservation_session.getEntityId()==0) {
            sqlQuery = "INSERT INTO PlanningDB.Reservation_Session (`uuid`, `sessionUuid`, `userUuid`) VALUES(\"" + reservation_session.getEntityId() + "\",\"" + reservation_session.getSessionUUID() + "\",\"" + reservation_session.getUserUUID() + "\");";
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


    //UPDATE
    public boolean updateReservationSessionByObject (Reservation_Session newReservationSessionFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.Reservation_Session SET " +
                "sessionUuid=\""+newReservationSessionFromMessage.getSessionUUID()+"\", " +
                "userUuid=\""+newReservationSessionFromMessage.getUserUUID()+"\", " +
                "paid="+newReservationSessionFromMessage.getPaid()+" " +

                "WHERE uuid=\""+newReservationSessionFromMessage.getReservationUUID()+"\" " +
                ";";
        boolean allGood = true;

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateReservationSessionByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newReservationSessionFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newReservationSessionFromMessage.getReservationId());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating reservation session  with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateReservationSessionByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\nInserting reservation session (ev."+newReservationSessionFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
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
