package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Task_DAO extends BaseEntityDAO {

    //CRUD Statements
    //CREATE
    public int insertIntoTask(Task task) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(task.getEntityId(), task.getEntityVersion(), task.getActive(), task.getTimestamp());

        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (task.getEntityId() != 0 && callbackInsertedInt != task.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + task.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        sqlQuery = "INSERT INTO PlanningDB.Task (`idTask`, `uuid`, `eventUuid`, `taskName`, `description`, `dateTimeStart`,`dateTimeEnd`, `GCAEventId`, `GCAEventLink`) VALUES (" + callbackInsertedInt + ",\"" + task.getTaskUuid() + "\",\"" + task.getEventUuid() + "\",\"" + task.getTaskName() + "\",\"" + task.getDescription() + "\",\"" + task.getDateTimeStart() + "\",\"" + task.getDateTimeEnd() + "\",\"" + task.getGCAEventId() + "\",\"" + task.getGCAEventLink() + "\");";

        int insertSuccess = BaseEntityDAO.runInsertQuery(sqlQuery);

        if(insertSuccess!=0){

            return callbackInsertedInt;

        }else{
            return callbackInsertedInt;
        }
    }

    //READ


    //UPDATE
    public boolean updateTaskByObject (Task newTaskFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.Task SET " +

                "description=\""+newTaskFromMessage.getDescription()+"\", " +
                "taskName=\""+newTaskFromMessage.getTaskName()+"\", " +
                "dateTimeStart=\""+newTaskFromMessage.getDateTimeStart()+"\", " +
                "dateTimeEnd=\""+newTaskFromMessage.getDateTimeEnd()+"\", " +
                "eventUuid=\""+newTaskFromMessage.getEventUuid()+"\" " +
                "WHERE uuid=\""+newTaskFromMessage.getTaskUuid()+"\" " +

                ";";
        boolean allGood = true;

        //System.out.println(sqlQuery);
        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateTaskByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newTaskFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newTaskFromMessage.getTaskId());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating Task with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateTaskByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\nInserting Task (ev."+newTaskFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
                if(statement != null)

                    statement.close();
            }catch(SQLException e){
                System.out.println(e.getMessage());
                throw new RuntimeException("ERROR 02: Error during closing the connection...");
            }
        }

    }


    //DELETE


    //OTHER


}
