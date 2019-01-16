package services;

import constants.FolderType;
import data.DatabaseTransaction;
import models.Folder;
import models.Mails;
import utils.Utils;
import view_objects.EmailItem;
import view_objects.ReplyToMailRequest;
import view_objects.SendEmailRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MailServices {

    DatabaseTransaction transactionService = new DatabaseTransaction();

    public CompletableFuture<String> sendEmail(SendEmailRequest sendEmailRequest){
        CompletableFuture<String> ss = new CompletableFuture<>();
        Integer mailId = Utils.generateMailId();
        Long epocTime = Instant.now().getEpochSecond();
        Integer threadId = Utils.generateThreadId();
        List<Folder> folderItems = getFolderListFromRequest(sendEmailRequest, threadId, mailId, epocTime);
        Mails mailItem = getMailModelFromRequest(sendEmailRequest, threadId, mailId, epocTime);

        try {
            transactionService.insertIntoFolderTable(folderItems);
            transactionService.insertIntoMailsTable(mailItem);
            ss.complete("Mail sent successfully");
        } catch (Exception e) {
            ss.complete("Mail sending failed");
        }
        return ss;
    }

    public CompletableFuture<String> replyToMail(ReplyToMailRequest replyEmailRequest){
        CompletableFuture<String> ss = new CompletableFuture<>();
        Long epocTime = Instant.now().getEpochSecond();
        List<Folder> folderItems = getFolderListFromRequest(replyEmailRequest, replyEmailRequest.threadId, replyEmailRequest.mailId, epocTime);
        Mails mailItem = getMailModelFromRequest(replyEmailRequest, replyEmailRequest.threadId, replyEmailRequest.mailId, epocTime);

        try {
            transactionService.insertIntoFolderTable(folderItems);
            transactionService.insertIntoMailsTable(mailItem);
            ss.complete("Mail sent successfully");
        } catch (Exception e) {
            ss.complete("Mail sending failed");
        }
        return ss;
    }

    private List<Folder> getFolderListFromRequest(SendEmailRequest sendEmailRequest, Integer tId, Integer mId, Long currTime) {
        List<Folder> folderModelObjects = new ArrayList<>();
        for(String toEmail : sendEmailRequest.tosList) {
            Folder folderItem = new Folder();
            folderItem.folder_type = FolderType.INBOX.getName();
            folderItem.email = toEmail.toString();
            folderItem.thread_id = tId;
            folderItem.mail_id = mId;
            folderItem.from_user = sendEmailRequest.fromUser;
            folderItem.time_stamp = currTime.intValue();
            folderItem.is_read = false;
            folderModelObjects.add(folderItem);
        }
        Folder folderItem = new Folder();
        folderItem.folder_type = FolderType.SENT.getName();
        folderItem.email = sendEmailRequest.fromUser.toString();
        folderItem.thread_id = tId;
        folderItem.mail_id = mId;
        folderItem.is_read = false;
        folderItem.time_stamp = currTime.intValue();
        folderItem.from_user = sendEmailRequest.fromUser;
        folderModelObjects.add(folderItem);
        return folderModelObjects;
    }


    private Mails getMailModelFromRequest(SendEmailRequest sendEmailRequest, Integer tId, Integer mId, Long currTime) {
        Mails mailModelObject = new Mails();
        mailModelObject.id = mId;
        mailModelObject.body = sendEmailRequest.body;
        mailModelObject.thread_id = tId;
        mailModelObject.time_stamp = currTime.intValue();
        return mailModelObject;
    }


    // Save to draft
    public CompletableFuture<String> saveToDraft(SendEmailRequest sendEmailRequest){
        CompletableFuture<String> ss = new CompletableFuture<>();
        Folder folderItem = new Folder();
        folderItem.folder_type = FolderType.DRAFT.getName();
        Integer mailId = Utils.generateMailId();
        Integer threadId = Utils.generateThreadId();
        Long epocTime = Instant.now().getEpochSecond();
        Mails mailItem = getMailModelFromRequest(sendEmailRequest, threadId, mailId, epocTime);
        folderItem.thread_id = threadId;
        folderItem.mail_id = mailId;
        folderItem.is_read = true;
        folderItem.email = sendEmailRequest.fromUser;
        try {
            transactionService.insertIntoFolderTable(Arrays.asList(folderItem));
            transactionService.insertIntoMailsTable(mailItem);
            ss.complete("Mail saved to draft successfully");
        } catch (Exception e) {
            ss.complete("Mail failed to save to draft");
        }
        return ss;
    }


    // delete item
    public CompletableFuture<String> deleteItem(Integer mailId) {
        CompletableFuture<String> ss = new CompletableFuture<>();
        System.out.println(mailId);
        try {
            transactionService.moveItemsAcrossFolderTypes(mailId, FolderType.TRASH.getName());
            ss.complete("Mail item deleted successfully");
        } catch (Exception e) {
            ss.complete("Mail failed to delete");
        }
        return ss;
    }

    // mark as read or unread
    public CompletableFuture<String> markReadStatusOfMail(Integer userMail, boolean markAsRead) {
        CompletableFuture<String> ss = new CompletableFuture<>();
        try {
            transactionService.markAsReadORUnread(userMail, markAsRead);
            ss.complete("Mail's read status changed to: "+markAsRead);
        } catch (Exception e) {
            ss.complete("Mail's read status failed to change");
        }
        return ss;
    }

    // get folder data for the user
    public CompletableFuture<String> getFolderDataForUser(String folderType, String emailAddress) {
        CompletableFuture<String> ss = new CompletableFuture<>();
        try {
            ArrayList<EmailItem> data = transactionService.getFolderData(folderType, emailAddress);
            ss.complete(utils.Utils.getJsonStringFromObject(data));
        } catch (Exception e) {
            ss.complete("Could not fetch data for userEmail "+emailAddress);
        }
        return ss;
    }

    // get folder data for the user
    public CompletableFuture<String> getMailboxDataForUser(String emailAddress) {
        CompletableFuture<String> ss = new CompletableFuture<>();
        try {
            ArrayList<EmailItem> data = transactionService.getAllEmailDataForUser(emailAddress);
            ss.complete(utils.Utils.getJsonStringFromObject(data));
        } catch (Exception e) {
            ss.complete("Could not fetch data for userEmail "+emailAddress);
        }
        return ss;
    }
}