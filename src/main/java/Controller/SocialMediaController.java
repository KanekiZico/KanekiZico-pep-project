package Controller;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccountHandler); //http://localhost:8080/register
        app.post("/login", this::userLoginHandler); //http://localhost:8080/login
        app.post("/messages", this::createMessageHandler); //http://localhost:8080/messages
        app.get("/messages", this::retrieveAllMessagesHandler); //http://localhost:8080/messages
        app.get("/messages/{message_id}", this::retrieveMessageById); //http://localhost:8080/messages/{message_id}
        app.delete("/messages/{message_id}", this::deleteMessageById); //http://localhost:8080/messages/{message_id}
        app.patch("/messages/{message_id}", this::updateMessageById); //http://localhost:8080/messages/{message_id}
        app.get("/accounts/{account_id}/messages", this::retrieveAllMessagesByUser); //http:localhost:8080/accounts/{account_id}/messages
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws SQLException 
     */
    private void createAccountHandler(Context context) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.createAccount(account);
        if(createdAccount != null)
        {
            context.json(mapper.writeValueAsString(createdAccount));
        }
        else
        {
            context.status(400);
        } 
    }

    private void userLoginHandler(Context context) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyUser(account);
        if(verifiedAccount != null)
        {
            context.json(mapper.writeValueAsString(verifiedAccount));
        }
        else
        {
            context.status(401);
        }
    }

    private void createMessageHandler(Context context) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage != null)
        {
            context.json(mapper.writeValueAsString(createdMessage));
        }
        else
        {
            context.status(400);
        }
        
    }

    private void retrieveAllMessagesHandler(Context context) throws SQLException{ 
        //List<Message> messages = messageService.getAllMessages();
        context.json(messageService.getAllMessages());
    }

    private void retrieveMessageById(Context context) throws SQLException{ 
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message != null)
            context.json(message);
    }

    private void deleteMessageById(Context context) throws SQLException{
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(message_id);
        if(deletedMessage != null)
            context.json(deletedMessage);
    }

    private void updateMessageById(Context context) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageById(message_id, message);
        if(updatedMessage != null)
        {
            context.json(mapper.writeValueAsString(updatedMessage));
        }
        else
        {
            context.status(400);
        }
    }
    
    private void retrieveAllMessagesByUser(Context context) throws SQLException{
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllMessagesByUser(account_id));

    }



}