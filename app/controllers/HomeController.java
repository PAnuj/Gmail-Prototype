package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.FormFactory;
import play.mvc.*;
import services.MailServices;
import view_objects.ReplyToMailRequest;
import view_objects.SendEmailRequest;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Inject
    FormFactory formFactory;
    MailServices mailServices = new MailServices();

    public CompletableFuture<Result> sendMail() throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        JsonNode request = request().body().asJson();
        play.data.Form<SendEmailRequest> requestForm = formFactory.form(SendEmailRequest.class);
        SendEmailRequest requestVO =  requestForm.bind(request).get();
        try{
            mailServices.sendEmail(requestVO).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error in sending mail "+ex);
        }
        return result;
    }

    public CompletableFuture<Result> replyToMail() throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        JsonNode request = request().body().asJson();
        play.data.Form<ReplyToMailRequest> requestForm = formFactory.form(ReplyToMailRequest.class);
        ReplyToMailRequest requestVO =  requestForm.bind(request).get();
        try{
            mailServices.replyToMail(requestVO).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error in replying to the mail "+ex);
        }
        return result;
    }

    public CompletableFuture<Result> saveToDraft() throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        JsonNode request = request().body().asJson();
        play.data.Form<SendEmailRequest> requestForm = formFactory.form(SendEmailRequest.class);
        SendEmailRequest requestVO =  requestForm.bind(request).get();
        try{
            mailServices.saveToDraft(requestVO).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error in sending mail "+ex);
        }
        return result;
    }
    public CompletableFuture<Result> deleteMail(Integer userEmail) throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        try{
            mailServices.deleteItem(userEmail).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error in sending mail "+ex);
        }
        return result;
    }

    public CompletableFuture<Result> markAsReadOrUnread(Integer userEmail, boolean status) throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        try{
            mailServices.markReadStatusOfMail(userEmail, status).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error change read status "+ex);
        }
        return result;
    }

    public CompletableFuture<Result> getFolderData(String folderType, String userEmail) throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        try{
            mailServices.getFolderDataForUser(folderType, userEmail).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error change read status "+ex);
        }
        return result;
    }

    public CompletableFuture<Result> fetchMailboxData(String userEmail) throws InterruptedException, ExecutionException, JsonProcessingException, IOException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        try{
            mailServices.getMailboxDataForUser(userEmail).thenApply(searchResults -> {
                result.complete(ok((searchResults)));
                return null;
            });
        }catch(Exception ex){
            play.Logger.error("Error change read status "+ex);
        }
        return result;
    }
}

