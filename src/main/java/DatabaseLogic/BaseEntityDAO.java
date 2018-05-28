package DatabaseLogic;

import AppLogic.Helper;

import java.sql.*;
import java.util.ArrayList;

public class BaseEntityDAO extends BaseDAO{

    //CRUD Statements
    //CREATE
    public static int insertIntoBaseEntity(BaseEntity thisBaseEntity) throws SQLException {

        PreparedStatement preparedStatement = null;
        String sqlQuery="";

        if(thisBaseEntity.getEntityId()==0)
        {
            //unknown BaseEntity-object for database
            sqlQuery = "INSERT INTO PlanningDB.BaseEntity (entity_version, active, timestamp) VALUES(1,1,\""+Helper.getCurrentDateTimeStamp()+"\");";
            //sqlQuery = "INSERT INTO planningdb.baseentity (entity_version, status, timestamp) VALUES(1,1,\""+Helper.getCurrentDateTimeStamp()+"\");";

        }else{
            //known BaseEntity-object for database
            sqlQuery = "INSERT INTO PlanningDB.BaseEntity (idBaseEntity, entity_version, active, timestamp) VALUES(\""+thisBaseEntity.getEntityId()+"\",\""+thisBaseEntity.getEntityVersion()+"\",\""+thisBaseEntity.getActive()+"\",\""+thisBaseEntity.getTimestamp()+"\");";
        }
        //INSERT INTO `PlanningDB`.`BaseEntity` (`idBaseEntity`, `entity_version`, `status`, `timestamp`) VALUES (NULL, NULL, NULL, NULL);

        //System.out.println("sqlQuery: "+sqlQuery+"\n");
        try {

            getConnection().isClosed();

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            preparedStatement = getConnection().prepareStatement(sqlQuery);
            //prepare statement here: preparedStatement.setString(1, f.getProperty());
            preparedStatement.execute(sqlQuery,Statement.RETURN_GENERATED_KEYS);
            ResultSet keyset = preparedStatement.getGeneratedKeys();
            int returnInsertedId=0;
            if ( keyset.next() ) {
                // Retrieve the auto generated key(s).
                returnInsertedId = keyset.getInt(1);
            }
            return returnInsertedId;

        }catch(SQLException e){

            System.out.println(e.getMessage());;
            //throw new RuntimeException(e.getMessage());

        }finally{

            try{

                if(preparedStatement != null)
                    preparedStatement.close();

            }catch(SQLException e){

                System.out.println(e.getMessage());;
                throw new RuntimeException("ERROR 02: Something seems to have gone wrong during closing the connection...");

            }
        }
        return runInsertQuery(sqlQuery);

    }

    //READ
    public BaseEntity getBaseEntityByEntityId(int thisEntityId) {
        ResultSet rs = null;
        BaseEntity thisBaseEntity = null;

        String sql = "SELECT * FROM PlanningDB.BaseEntity WHERE idBaseEntity = \""+thisEntityId+"\";";

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sql);
            if(rs.next())
            {
                thisBaseEntity = new BaseEntity(rs.getInt(1),rs.getInt(2),rs.getInt(3), rs.getString(4));

                return thisBaseEntity;
            }else{
                throw new IllegalStateException("ERROR 03: No entity found with id: '"+thisEntityId+"'...");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    public String[] getPropertyValueByTableAndProperty(String[] propertiesToSelect, String table, String[] selectors, String[] values) {
        ResultSet rs = null;
        BaseEntity thisBaseEntity = null;


        String sql = "SELECT ";
        for (int i=0;i<propertiesToSelect.length;i++)
        {
            sql+=propertiesToSelect[i];

            if(i<propertiesToSelect.length-1)
            {
                sql+=", ";
            }
        }

        sql += " FROM PlanningDB."+table+" WHERE ";

        for (int i=0;i<selectors.length;i++)
        {
            sql+=selectors[i]+" = \""+values[i]+"\"";

            if(i<selectors.length-1)
            {
                sql+=" AND ";
            }
        }
        sql+=";";

        //System.out.println("sqlquery: "+sql);

        try(Statement s = getConnection().createStatement()){

            // Tweaked from https://coderwall.com/p/609ppa/printing-the-result-of-resultset

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnsNumber = rsmd.getColumnCount();
            String[] resultStringArray = new String[columnsNumber];
            int resultCounter=0;

            if(columnsNumber>0)
            {
                while(rs.next())
                {
                    String thisRowString ="";

                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1 && i < columnsNumber)
                        {
                            thisRowString+="', '";
                        }else if(i>1){

                            thisRowString+="';";
                        }else{

                            //thisRowString+="'";
                        }
                        String columnValue = rs.getString(i);
                        //thisRowString+=columnValue + " " + rsmd.getColumnName(i);

                        thisRowString+=columnValue;
                        }

                    resultStringArray[resultCounter] = thisRowString;

                    resultCounter++;
                    //thisBaseEntity = new BaseEntity(rs.getInt(1),rs.getInt(2),rs.getString(3), rs.getString(4));

                }
                return resultStringArray;

            }else{

                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //UPDATE
    public boolean updateTablePropertyValue(String table, String property, String value, String valueType, String whereProperty, String whereValue) {
        ResultSet rs = null;
        Boolean executeSucces = false;

        if(valueType=="String")
        {
            value = "\""+value+"\"";
            whereValue="\""+whereValue+"\"";
        }

        String sql = "UPDATE PlanningDB."+table+" SET "+property+" = "+value+" WHERE "+whereProperty+" = "+whereValue+";";

        //System.out.println("sql-query: "+sql);

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection closed...");
            }
            statement = getConnection().prepareStatement(sql);
            try{
                statement.executeUpdate();
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement.executeUpdate():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\n"+sql+"\nEND\n");
                if(statement != null)
                    statement.close();
            }catch(SQLException e){
                System.out.println(e.getMessage());;
                throw new RuntimeException("ERROR 02: Error during closing the connection...");
            }
        }

    }

    //DELETE
    public boolean softDeleteBaseEntity(int entityId) {
        ResultSet rs = null;
        Boolean executeSucces = false;

        String sql = "UPDATE PlanningDB.BaseEntity SET active = 0, entity_version = entity_version + 1 WHERE idBaseEntity = "+entityId+";";
        //String sql = "UPDATE PlanningDB.? SET ? = ? WHERE idUser = ?;";

        //System.out.println("sql: "+sql);

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }
            statement = getConnection().prepareStatement(sql);
            try{
                statement.executeUpdate();

                return true;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
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

    //OTHER
    public boolean doesUUIDExist(String tableToCheck, String UUID) {
        ResultSet rs = null;
        Boolean uuidExists = false;
        String idFromTableToCheck ="id";

        //if table contains a _ delete it
        if(tableToCheck.contains("_")){

            String[] parts = tableToCheck.split("_");

            for (int i=0; i< parts.length;i++)
            {
                idFromTableToCheck+=parts[i];
            }

        }else{
            idFromTableToCheck+=tableToCheck;
        }
                                // SELECT name FROM PlanningDB.User WHERE idUser = 5 AND active = 1;
        String sql = "SELECT uuid FROM PlanningDB."+tableToCheck+" t1 JOIN PlanningDB.BaseEntity t2 ON t1."+idFromTableToCheck+" = t2.idBaseEntity WHERE uuid = \""+UUID+"\" AND active = 1;";

        //System.out.println("test: "+tableToCheck.substring(0, 11).toLowerCase()+ " // does uuid exist sql: "+sql);

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
    public static int runInsertQuery(String sqlQuery) {
        PreparedStatement preparedStatement = null;
        try {

            getConnection().isClosed();

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            preparedStatement = getConnection().prepareStatement(sqlQuery);
            //prepare statement here: preparedStatement.setString(1, f.getProperty());
            preparedStatement.execute(sqlQuery,Statement.RETURN_GENERATED_KEYS);
            ResultSet keyset = preparedStatement.getGeneratedKeys();
            int returnInsertedId=0;
            if ( keyset.next() ) {
                // Retrieve the auto generated key(s).
                returnInsertedId = keyset.getInt(1);
            }
            return returnInsertedId;

        }catch(SQLException e){

            System.out.println(e.getMessage());;
            //throw new RuntimeException("SQL error in BaseEntityDAO: "+e.getMessage());
            return 0;
        }finally{

            try{

                if(preparedStatement != null)
                    preparedStatement.close();

            }catch(SQLException e){

                System.out.println(e.getMessage());;
                //throw new RuntimeException("ERROR 02: Something seems to have gone wrong during closing the connection...");
                return 0;
            }
        }
    }
    public boolean isActive1(String tableToCheck, String UUID) {
        ResultSet rs = null;
        Boolean activeIs1 = false;
        String idFromTableToCheck ="id";

        if(tableToCheck.contains("_")){

            String[] parts = tableToCheck.split("_");

            for (int i=0; i< parts.length;i++)
            {
                idFromTableToCheck+=parts[i];
            }

        }else{
            idFromTableToCheck+=tableToCheck;
        }
        // SELECT name FROM PlanningDB.User WHERE idUser = 5 ;
        String sql = "SELECT active FROM PlanningDB."+tableToCheck+" t1 JOIN PlanningDB.BaseEntity t2 ON t1."+idFromTableToCheck+" = t2.idBaseEntity WHERE uuid = \""+UUID+"\";";

        //System.out.println("test: "+tableToCheck.substring(0, 11).toLowerCase()+ " // does uuid exist sql: "+sql);

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sql);
            if(rs.getInt("active") == 1)
            {
                activeIs1=true;

                return activeIs1;
            }else{
                activeIs1=false;

                return activeIs1;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
