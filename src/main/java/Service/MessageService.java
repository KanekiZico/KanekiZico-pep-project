package Service;

//import org.eclipse.jetty.http.HttpTester.Message;
import Model.Message;

import java.sql.SQLException;

import java.util.List;
import DAO.MessageDAO;


public class MessageService {
    private MessageDAO messageDAO;

    public MessageService()
    {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO)
    {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) throws SQLException {
        if(message.getMessage_text() == "")
        {
            System.out.println("The message should not be blank!");
            return null;
        }
        if(message.getMessage_text().length() > 255)
        {
            System.out.println("The message should be less than 255 characters!");
            return null;
        }
        Message m = this.messageDAO.verifyUser(message.getPosted_by(), message);
        if(m != null)
            return this.messageDAO.createMessage(m);
        return null;
        
    }
    public List<Message> getAllMessages() throws SQLException { 
        return this.messageDAO.getAllMessages();
    }
    public Message getMessageById(int message_id) throws SQLException {
        return this.messageDAO.getMessageById(message_id);
    }
    public Message deleteMessageById(int message_id) throws SQLException {
        Message message = this.messageDAO.getMessageById(message_id);
        if(message != null)
        {
            if(this.messageDAO.deleteMessageById(message_id)); //if the deletion was successfull, returns true
                return message;
        }
        return null;
    }
    public Message updateMessageById(int message_id, Message message) throws SQLException {
        if(message.getMessage_text() == "")
        {
            System.out.println("The message should not be blank!");
            return null;
        }
        if(message.getMessage_text().length() > 255)
        {
            System.out.println("The message should be less than 255 characters!");
            return null;
        }
        Message oldMessage = this.messageDAO.getMessageById(message_id);
        if(oldMessage != null)
        { 
            if(this.messageDAO.updateMessageById(message_id, message))
            {
                return this.messageDAO.getMessageById(message_id);
            }
        }
        return null;
    }
    public List<Message> getAllMessagesByUser(int account_id) throws SQLException {
        return this.messageDAO.getAllMessagesByUser(account_id);
        
    }

}
