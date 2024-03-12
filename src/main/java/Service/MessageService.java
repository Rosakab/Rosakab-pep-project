package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;



public class MessageService {
    private MessageDAO messageDAO;


public MessageService(){
    messageDAO = new MessageDAO();
}

public MessageService(MessageDAO messageDAO){
    this.messageDAO = messageDAO;

}

public Message addMessage(int posted_by, String message_text, long time_posted_epoch) {
    if (message_text == null) {
       
        return null;
} 
if(message_text.isBlank()){
    return null;
}
if(message_text.length() > 255){
    return null;
}
 return messageDAO.addMessage(posted_by, message_text, time_posted_epoch);
}


public List<Message> getAllMessages() {
    return messageDAO.getAllMessages();
}


public Message getMessageById(int messageId) {
    return messageDAO.getMessageById(messageId);
}


public Message deleteMessageByMessageId(int messageId) {
    Message mess= messageDAO.getMessageById(messageId);
    if(mess != null && messageDAO.deleteMessageByMessageId(messageId)){
        return mess;
    }
    return null;
}





public Message updateMessageByMessageId(int messageId, String newMessageText) {
    if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
        
        return null;
    }
    Message getMess= messageDAO.getMessageById(messageId);
    if(getMess ==null){
        return null;
    }
    return messageDAO.updateMessageByMessageId(messageId, newMessageText);
}


public List<Message> getMessagesByUserId(int userId) {
    return messageDAO.getMessagesByUserId(userId);
}


}