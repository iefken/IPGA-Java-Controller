package DatabaseLogic;

import AppLogic.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class User_DAO extends BaseEntityDAO {

    //CRUD Statements

    //CREATE

    public int insertIntoUser(User user) throws SQLException {

        BaseEntity newBaseEntity = new BaseEntity(user.getEntityId(), user.getEntityVersion(), user.getActive(), user.getTimestamp());

        //execute baseEntity Insert
        int callbackInsertedInt = newBaseEntity.getEntityId();

        if (user.getEntityId()!=0 && callbackInsertedInt != user.getEntityId()) {
            throw new SQLException("ERROR 05: Given id(" + user.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
        }

        //System.out.println("TEST: Given id(" + user.getEntityId() + ") retreived id(" + callbackInsertedInt + ")!");
        PreparedStatement preparedStatement = null;
        String sqlQuery = "";

        sqlQuery = "INSERT INTO PlanningDB.User (`idUser`, `uuid`, `lastName`, `firstName`, `phonenumber`, `email`, `street`,`houseNr`,`city`,`postalCode`, `country`, `company`, `type`) VALUES (" + callbackInsertedInt + ",\"" + user.getUuid() + "\",\"" + user.getLastname() + "\",\"" + user.getFirstname() + "\",\"" + user.getPhoneNumber() + "\",\"" + user.getEmail() + "\",\"" + user.getStreet() + "\",\"" + user.getHouseNr() + "\",\"" + user.getCity() + "\",\"" + user.getPostalCode() + "\",\"" + user.getCountry() + "\",\""+user.getCompany() + "\",\""+user.getUserType() + "\");";

        int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);
        //System.out.println("insertSucces: "+insertSucces+", callbackInsertedInt: "+callbackInsertedInt);
        return callbackInsertedInt;

    }

    //READ

    public BaseEntity getUserByUserId(int thisUserId)
    {
        ResultSet rs = null;
        User thisUser = null;

        String sql = "SELECT * FROM PlanningDB.User t1 JOIN PlanningDB.BaseEntity t2 ON t1.idUser = t2.idBaseEntity WHERE idUser = \""+thisUserId+"\";";

        try(Statement s = getConnection().createStatement()){

            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sql);
            if(rs.next())
            {
                // for our enum: tweaked from https://stackoverflow.com/questions/3155967/are-enums-supported-by-jdbc
                // Helper.EntityType.valueOf(rs.getString("type")

                thisUser = new User(rs.getInt(1),rs.getInt(15),rs.getInt(16), rs.getString(17),rs.getString(2),rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString("userType"));

                return thisUser;
            }else{
                throw new IllegalStateException("ERROR 03: No entity found with id: '"+thisUserId+"'...");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    //UPDATE

    public boolean updateUserByObject (User newUserFromMessage) {

        String sqlQuery = " UPDATE PlanningDB.User SET " +
                "lastName=\""+newUserFromMessage.getLastName()+"\", " +
                "firstName=\""+newUserFromMessage.getFirstName()+"\", " +
                "phonenumber=\""+newUserFromMessage.getPhoneNumber()+"\", " +
                "email=\""+newUserFromMessage.getEmail()+"\", " +
                "street=\""+newUserFromMessage.getStreet()+"\", " +
                "houseNr=\""+newUserFromMessage.getHouseNr()+"\", " +
                "city=\""+newUserFromMessage.getCity()+"\", " +
                "postalCode=\""+newUserFromMessage.getPostalCode()+"\", " +
                "country=\""+newUserFromMessage.getCountry()+"\", " +
                "company=\""+newUserFromMessage.getCompany()+"\", " +
                "type=\""+newUserFromMessage.getUserType()+"\" " +

                "WHERE uuid=\""+newUserFromMessage.getUuid()+"\" " +

                ";";
        boolean allGood = true;

        PreparedStatement statement = null;
        try {
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR: Connection closed in updateUserByObject...");
            }
            statement = getConnection().prepareStatement(sqlQuery);
            try{
                statement.executeUpdate();

                try {

                    allGood = new BaseEntityDAO().updateTablePropertyValue("BaseEntity", "entity_version", "" + newUserFromMessage.getEntityVersion(), "int", "idBaseEntity", "" + newUserFromMessage.getIdUser());

                } catch (Exception e) {
//                e.printStackTrace();
                    System.out.println("ERROR updating User with query:<\n"+sqlQuery+"\n>\n" + e);
                }
                return true;
            }catch (Exception e) {
                System.out.println("ERROR: during executing statement: updateUserByObject():\n"+e);
                //e.printStackTrace();
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());;
            throw new RuntimeException(e.getMessage());
        }finally{
            try{
                //System.out.println("Queried:\nSTART\nInserting User (ev."+newUserFromMessage.getEntityVersion()+") with query:<\n"+sqlQuery+"\n>\nEND\n");
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