package Controller;

import Service.AccountService;

import java.util.List;


import Model.Account;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.MessageService;
import Model.Message;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;


    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }


    public SocialMediaController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
      
        //endpoints for Account
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);


        //endpoints for Message 
        app.post("/messages", this::addMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

     // ....................  handelers for Account................................................

    
    private void registerHandler(Context context)  {
           
        try {
            Account accountBody = context.bodyAsClass(Account.class);
            Account addedAccount = accountService.accountNewUser(accountBody.username, accountBody.password);
            if (addedAccount != null) {
                context.json(addedAccount);
            } else {
                context.status(400);
            }
        } catch (Exception e) {
          
            System.out.println(e.getMessage());

        }
            
        }
   


        private void loginHandler(Context context) {
          
            try {
                Account accountBody = context.bodyAsClass(Account.class);
                Account userLogin = accountService.loginValidation(accountBody.username, accountBody.password);
                if (userLogin != null) {
                    context.json(userLogin);
                } else {
                    context.status(401); 
                }
            } catch (Exception e) {
                
                System.out.println(e.getMessage());

            }
                
        }





//........................................handler for Message Class..............................


 private void addMessageHandler(Context context) {
  
        Message messageBody = context.bodyAsClass(Message.class);
        Message addedMessage = messageService.addMessage(
                
                messageBody.getPosted_by(),
                messageBody.getMessage_text(),
                messageBody.getTime_posted_epoch()
        );
        if (addedMessage != null) {
            context.json(addedMessage);
        } else {
            context.status(400);
        }
    
}




// ...............................................................................

private void getAllMessagesHandler(Context context) {
    context.json(messageService.getAllMessages());
}


//......................................................

private void getMessageByIdHandler(Context context) {
    
        // Message messageBody = context.bodyAsClass(Message.class);
         int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            context.json(message);
        } else {
            context.result("");
        }
   
}

//.................................................................

private void deleteMessageHandler(Context context) {

    
        // Message messageBody = context.bodyAsClass(Message.class);
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deleted = messageService.deleteMessageByMessageId(messageId);
        if (deleted != null) {
            context.json(deleted);
        } else {
            context.result("");
        }
    
    
}


private void updateMessageHandler(Context context) {
  
        // Message messageBody = context.bodyAsClass(Message.class);
        // int messageId = context.pathParam("message_id", Integer.class).get();

        String updatedMessage = context.bodyAsClass(Message.class).getMessage_text();
        int messageId = Integer.parseInt(context.pathParam("message_id"));

        Message success = messageService.updateMessageByMessageId(messageId, updatedMessage );
        

        if (success != null) {
            context.json(success);
        } else {
            context.status(400).result("");
        }
    
}




private void getMessagesByUserIdHandler(Context context) {
    
        // Message messageBody = context.bodyAsClass(Message.class);
        // int accountId = context.pathParam("account_id", Integer.class).get();
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> userMessages = messageService.getMessagesByUserId(accountId);


        context.json(userMessages);

}


}
