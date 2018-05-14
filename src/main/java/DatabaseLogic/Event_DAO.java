package DatabaseLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Event_DAO extends BaseEntityDAO{
        //CRUD Statements

        //CREATE

        public int insertIntoEvent(Event event) throws SQLException {

            BaseEntity newBaseEntity = new BaseEntity(event.getEntityId(), event.getEntityVersion(), event.getActive(), event.getTimestamp());

            //execute baseEntity Insert
            int callbackInsertedInt = newBaseEntity.getEntityId();

            if (event.getEntityId()!=0 && callbackInsertedInt != event.getEntityId()) {
                throw new SQLException("ERROR 05: Given id(" + event.getEntityId() + ") does not correspond to retreived id(" + callbackInsertedInt + ")!");
            }

            PreparedStatement preparedStatement = null;
            String sqlQuery = "";

            sqlQuery = "INSERT INTO PlanningDB.Event (`idEvent`, `eventUUID`, `eventName`, `maxAttendees`, `description`, `summary`, `location`,`contactPerson`,`dateTimeStart`,`dateTimeEnd`, `type`, `price`, `eventLink`) VALUES (" + callbackInsertedInt + ",\"" + event.getEventUUID() + "\",\"" + event.getEventName() + "\",\"" + event.getMaxAttendees() + "\",\"" + event.getDescription() + "\",\"" + event.getSummary() + "\",\"" + event.getLocation() + "\",\"" + event.getContactPerson() + "\",\"" + event.getDateTimeStart() + "\",\"" + event.getDateTimeEnd()+ "\",\"" + event.getType() + "\",\""+ event.getPrice() + "\",\""+ event.getEventLink() + "\");";

            //INSERT INTO `PlanningDB`.`Session` (`idSession`, `sessionUUID`, `eventUUID`, `sessionName`, `maxAttendees`, `dateTimeStart`, `dateTimeEND`, `speaker`, `local`, `type`) VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

            int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);

            return callbackInsertedInt;

        }

        //READ


        //UPDATE

        public int UpdateEvent(Event newEventFromMessage, int oldEntityId) throws SQLException {

            //Maak een nieuwe BaseEntity met incremented entityVersion
            BaseEntity newBaseEntity = new BaseEntity(newEventFromMessage.getEntityId(), newEventFromMessage.getEntityVersion() , newEventFromMessage.getActive(), newEventFromMessage.getTimestamp());
            //execute baseEntity Insert
            int callbackInsertedInt = newBaseEntity.getEntityId();

            String sqlQuery = "INSERT INTO PlanningDB.Event (idEvent, eventUUID, eventName, maxAttendees, description, summary, location,`contactPerson`,`dateTimeStart`,`dateTimeEnd`, type, price) VALUES (" + callbackInsertedInt + ",\"" + newEventFromMessage.getEventUUID() + "\",\"" + newEventFromMessage.getEventName() + "\",\"" + newEventFromMessage.getMaxAttendees() + "\",\"" + newEventFromMessage.getDescription() + "\",\"" + newEventFromMessage.getSummary() + "\",\"" + newEventFromMessage.getLocation() + "\",\"" + newEventFromMessage.getContactPerson() + "\",\"" + newEventFromMessage.getDateTimeStart() + "\",\"" + newEventFromMessage.getDateTimeEnd()+ "\",\"" + newEventFromMessage.getType() + "\",\""+ newEventFromMessage.getPrice() + "\");";

            //softdelete oude base entity
            softDeleteBaseEntity("Event",oldEntityId);
            int insertSucces = BaseEntityDAO.runInsertQuery(sqlQuery);

            return callbackInsertedInt;

        }

        //DELETE


        //OTHER




}
