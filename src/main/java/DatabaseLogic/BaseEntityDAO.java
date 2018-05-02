package DatabaseLogic;

import Logic.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Statement;

public class BaseEntityDAO extends BaseDAO{

    //CRUD Statements

    //CREATE

    public static int insertIntoBaseEntity(BaseEntity thisBaseEntity) throws SQLException {

        PreparedStatement preparedStatement = null;
        String sqlQuery="";

        if(thisBaseEntity.getEntityId()==0)
        {
            sqlQuery = "INSERT INTO PlanningDB.baseentity (entity_version, status, timestamp) VALUES(1,1,\""+Helper.getCurrentDateTimeStamp()+"\");";
        }else{
            sqlQuery = "INSERT INTO PlanningDB.baseentity (entityId, entity_version, status, timestamp) VALUES(\""+thisBaseEntity.getEntityId()+"\",\""+thisBaseEntity.getEntity_version()+"\",\""+thisBaseEntity.getStatus()+"\",\""+thisBaseEntity.getTimestamp()+"\");";
        }
        //INSERT INTO `PlanningDB`.`BaseEntity` (`entityId`, `entity_version`, `status`, `timestamp`) VALUES (NULL, NULL, NULL, NULL);

        return runInsertQuery(sqlQuery);

    }

    //READ

    public BaseEntity getBaseEntityByEntityId(int thisEntityId)
    {
        ResultSet rs = null;
        BaseEntity thisBaseEntity = null;

        String sql = "SELECT * FROM BaseEntity WHERE entityId = \""+thisEntityId+"\";";

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sql);
            if(rs.next())
            {
                thisBaseEntity = new BaseEntity(rs.getInt(1),rs.getInt(2),rs.getString(3), rs.getString(4));

                return thisBaseEntity;
            }else{
                throw new IllegalStateException("ERROR 03: No entity found with id: '"+thisEntityId+"'...");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //UPDATE



    //DELETE



    //OTHER

    public boolean doesUUIDExist(String tableToCheck, String UUID)
    {
        ResultSet rs = null;
        Boolean uuidExists = false;

        String sql = "SELECT \""+tableToCheck.toLowerCase()+"UUID"+"\" FROM "+tableToCheck+" WHERE \""+tableToCheck.toLowerCase()+"UUID"+"\" = \""+UUID+"\";";

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sql);
            if(rs.next())
            {
                uuidExists=true;

                return uuidExists;
            }else{
                uuidExists=false;

                return uuidExists;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static int runInsertQuery(String sqlQuery)
    {

        Statement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            statement = getConnection().createStatement();
            //prepare statement here: preparedStatement.setString(1, f.getProperty());
            statement.execute(sqlQuery,Statement.RETURN_GENERATED_KEYS);
            ResultSet keyset = statement.getGeneratedKeys();
            int returnInsertedId=0;
            if ( keyset.next() ) {
                // Retrieve the auto generated key(s).
                returnInsertedId = keyset.getInt(1);
            }
            return returnInsertedId;

        }catch(SQLException e){

            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());

        }finally{

            try{

                if(statement != null)
                    statement.close();

            }catch(SQLException e){

                System.out.println(e.getMessage());;
                throw new RuntimeException("ERROR 02: Something seems to have gone wrong during closing the connection...");

            }
        }
    }


}
