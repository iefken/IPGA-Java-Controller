package DatabaseLogic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntitiesToAdd_DAO extends BaseEntityDAO {

    //CRUD Statements
    //CREATE


    //READ

    public List<EntitiesToAdd> getRecordsFromEntitiesToAdd() {

        ResultSet rs = null;
        String sqlQuery = "";

        sqlQuery = "SELECT PlanningDB.EntitiesToAdd.idEntitiesToAdd, PlanningDB.EntitiesToAdd.table, PlanningDB.EntitiesToAdd.status, PlanningDB.BaseEntity.entity_version, PlanningDB.BaseEntity.active " +
                "FROM PlanningDB.EntitiesToAdd " +
                "JOIN PlanningDB.BaseEntity " +
                "ON PlanningDB.EntitiesToAdd.idEntitiesToAdd = BaseEntity.idBaseEntity  WHERE `status` != \"UPTODATE\" ;";
        /*
        SELECT PlanningDB.EntitiesToAdd.idEntitiesToAdd, PlanningDB.EntitiesToAdd.table, PlanningDB.EntitiesToAdd.status, PlanningDB.BaseEntity.idBaseEntity, PlanningDB.BaseEntity.entity_version, PlanningDB.BaseEntity.active
FROM PlanningDB.EntitiesToAdd
JOIN PlanningDB.BaseEntity
ON EntitiesToAdd.idEntitiesToAdd = BaseEntity.idBaseEntity
WHERE `status` !="UPTODATE";
         */

        boolean allGood = false;

        try (Statement s = getConnection().createStatement()) {

            // Tweaked from https://coderwall.com/p/609ppa/printing-the-result-of-resultset
            if (getConnection().isClosed()) {
                throw new IllegalStateException("ERROR 01: Connection seems to be closed...");
            }

            rs = s.executeQuery(sqlQuery);
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnsNumber = rsmd.getColumnCount();
            String[] resultStringArray = new String[columnsNumber];
            int resultCounter = 0;

            if (columnsNumber > 0) {
                List<EntitiesToAdd> entitiesToAddList = new ArrayList<>() ;

                while (rs.next()) {

                    EntitiesToAdd newEntityToAdd = null;
                    try {
                        newEntityToAdd = new EntitiesToAdd(rs.getInt("idEntitiesToAdd"),rs.getString("table"),EntitiesToAdd.EntityToAddStatus.valueOf(rs.getString("status")));
                    } catch (SQLException e) {
                        System.out.println("Error: 'SQLException' while getting EntitiesToAdd from rs: "+e);
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: 'IllegalArgumentException' while getting EntitiesToAdd from rs: "+e);
                        e.printStackTrace();
                    }

                    entitiesToAddList.add(newEntityToAdd);

                    resultCounter++;
                }
                return entitiesToAddList;

            } else {

                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //UPDATE

    //DELETE
}
