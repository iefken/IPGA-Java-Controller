package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Assign_Task_DAO extends BaseEntityDAO {

    //CRUD Statements
    //CREATE
    public int insertIntoAssign_Task(Assign_Task Assign_Task) throws SQLException {

        //execute baseEntity Insert
        BaseEntity newBaseEntity = new BaseEntity(Assign_Task.getEntityId(), Assign_Task.getEntityVersion(), Assign_Task.getActive(), Assign_Task.getTimestamp());

        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (Assign_Task.getEntityId()!=0 && callbackInsertedInt != Assign_Task.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + Assign_Task.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        //System.out.println("TEST: Given id(" + assign_task.getEntityId() + ") retreived id(" + callbackInsertedInt + ")!");
        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        if(Assign_Task.getEntityId()==0) {
            sqlQuery = "INSERT INTO PlanningDB.Assign_Task (`uuid`, `taskUuid`, `userUuid`) VALUES(\"" + Assign_Task.getAssignTaskUuid() + "\",\"" + Assign_Task.getTaskUuid() + "\",\"" + Assign_Task.getUserUuid() + "\");";
        }else{
            sqlQuery = "INSERT INTO PlanningDB.Assign_Task (`idAssignTask`, `uuid`, `taskUuid`, `userUuid`) VALUES(" + callbackInsertedInt + ",\"" + Assign_Task.getAssignTaskUuid() + "\",\"" + Assign_Task.getTaskUuid() + "\",\"" + Assign_Task.getUserUuid() + "\");";
        }

        int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        //System.out.println("insertSucces: "+insertSucces+", callbackInsertedInt: "+callbackInsertedInt);
        return callbackInsertedInt;

    }

    //READ


    //UPDATE
    public boolean updateAssignTaskByObject (Assign_Task newAssignTaskFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.Assign_Task SET " +
                "taskUuid=\""+newAssignTaskFromMessage.getTaskUuid()+"\", " +
                "userUuid=\""+newAssignTaskFromMessage.getUserUuid()+"\" " +

                "WHERE uuid=\""+newAssignTaskFromMessage.getAssignTaskUuid()+"\" " +
                ";";
        boolean allGood = true;

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateAssignTaskByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newAssignTaskFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newAssignTaskFromMessage.getAssignTaskId());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating assign task with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateAssignTaskByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\nInserting assign_task (ev."+newAssignTaskFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
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
