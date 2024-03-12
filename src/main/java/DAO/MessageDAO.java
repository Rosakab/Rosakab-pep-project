



// ## 3: Our API should be able to process the creation of new messages.

// As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.

// - The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
// - If the creation of the message is not successful, the response status should be 400. (Client error)

// ## 4: Our API should be able to retrieve all messages.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

// - The response body should contain a JSON representation of a list containing all messages retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.

// ## 5: Our API should be able to retrieve a message by its ID.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

// - The response body should contain a JSON representation of the message identified by the message_id. It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.

// ## 6: Our API should be able to delete a message identified by a message ID.

// As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

// - The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the now-deleted message. The response status should be 200, which is the default.
// - If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.

// ## 7: Our API should be able to update a message text identified by a message ID.

// As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.

// - The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
// - If the update of the message is not successful for any reason, the response status should be 400. (Client error)

// ## 8: Our API should be able to retrieve all messages written by a particular user.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

// - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.




package DAO;
import  Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class MessageDAO {

//## 3: Our API should be able to process the creation of new messages.

public Message addMessage( int posted_by, String message_text, long time_posted_epoch){
    Connection connection= ConnectionUtil.getConnection();
 

try{

    String sql= "Insert into message ( posted_by, message_text, time_posted_epoch) values (?,?,?)";
  
  
    PreparedStatement preparedStatement= connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          
            preparedStatement.setInt(1,posted_by);
            preparedStatement.setString(2,message_text);
            preparedStatement.setLong(3,time_posted_epoch);

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_messageId =  pkeyResultSet.getInt(1);
                return new Message(generated_messageId, posted_by, message_text,time_posted_epoch);

            } 
        }catch(SQLException e){
              System.out.println(e.getMessage());
}

          return null;
}


//## 4: Our API should be able to retrieve all messages.

public List<Message> getAllMessages(){
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
     
    try{
        String sql = "Select * from message";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultset = preparedStatement.executeQuery();
        
        while(resultset.next()){
            Message message = new Message(resultset.getInt("message_id"),
            resultset.getInt("posted_by"),
            resultset.getString("message_text"),
            resultset.getLong("time_posted_epoch"));
            messages.add(message);
        }


    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return messages;
}

//## 5: Our API should be able to retrieve a message by its ID.

public Message getMessageById(int message_id){
    Connection connection = ConnectionUtil.getConnection();

    try{

        String sql = "Select * from message where message_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        

        preparedStatement.setInt(1, message_id);

        ResultSet resultset = preparedStatement.executeQuery();
        if(resultset.next()){
            int messageId = resultset.getInt("message_id");
            int postBy= resultset.getInt("posted_by");
            String messageText= resultset.getString("message_text");
           long timePostedEpoch= resultset.getLong("time_posted_epoch");

           return new Message(messageId, postBy, messageText, timePostedEpoch);

    }
}catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}

//## 6: Our API should be able to delete a message identified by a message ID.

public boolean deleteMessageByMessageId(int message_id){
    Connection connection = ConnectionUtil.getConnection();
    boolean del = false;

    try{

        String sql = "DELETE FROM message WHERE message_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            int rowsAffected = preparedStatement.executeUpdate();

           del= rowsAffected > 0;


    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return del;
}

//## 7: Our API should be able to update a message text identified by a message ID.

public Message updateMessageByMessageId(int message_id, String message_text){
    Connection connection = ConnectionUtil.getConnection();
     Message updateMess= null;

    try {

        String sql = "Update message set message_text=? WHERE message_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
           
            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, message_id);

            int rowsAffected = preparedStatement.executeUpdate();

            
         if(rowsAffected >0){
            updateMess = getMessageById(message_id);
         }
        
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return updateMess;
}

//## 8: Our API should be able to retrieve all messages written by a particular user.


public List<Message> getMessagesByUserId(int posted_by){
    Connection connection = ConnectionUtil.getConnection();
    List<Message> userMessages = new ArrayList<>();

    try {

        String sql = "Select* from message where posted_by=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, posted_by);
        ResultSet resultset = preparedStatement.executeQuery();

        if (resultset.next()) {
            int messageId = resultset.getInt("message_id");
            int postedBy = resultset.getInt("posted_by");
            String messageText = resultset.getString("message_text");
            long timePostedEpoch = resultset.getLong("time_posted_epoch");

            userMessages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
        }
        
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return userMessages;
}


}
