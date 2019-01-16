package data;

import io.ebean.*;
import models.Folder;
import models.Mails;
import models.User;
import view_objects.EmailItem;

import java.util.*;

public class DatabaseTransaction {

    public void insertIntoFolderTable(List<Folder> folderItems) {
        Transaction transaction = Folder.db().beginTransaction();
        transaction.setBatchMode(true);
        transaction.setBatchSize(folderItems.size());
        try {
            for(Folder ebean: folderItems) {
                ebean.save();
            }
            transaction.commit();
        } catch (Exception e) {
            System.out.println("error occured "+e);
        } finally {
            transaction.end();
        }
    }

    public void insertIntoMailsTable(Mails mailItem) {
        Transaction transaction = Mails.db().beginTransaction();
        try {
            mailItem.save();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("error occured again "+ e);
        } finally {
            transaction.end();
        }
    }

    public void insertIntoUserTable(User user) {
        Transaction transaction = User.db().beginTransaction();
        try {
            user.save();
            transaction.commit();
        } catch (Exception e) {

        } finally {
            transaction.end();
        }
    }

    public void moveItemsAcrossFolderTypes(Integer mailId, String destinationFolder) {
        String sql = "update folder set folder_type = \""+destinationFolder+"\" where id ="+mailId+";";
        try {
            Update update = Ebean.createUpdate(Folder.class, sql);
            update.execute();
        } catch (Exception ee) {
            System.out.println("Error occurred while moving items " + ee);
        }
    }

    public void markAsReadORUnread(Integer mailId, boolean isRead) {
        String sql = "update folder set is_read = "+isRead+" where  id = "+mailId+";";
        try {
            Update update = Ebean.createUpdate(Folder.class, sql);
            update.execute();
        } catch (Exception e) {
            System.out.println("Error occurred while marking read status if mail");
        }
    }

    public ArrayList<EmailItem> getFolderData(String folderType, String emailAddress) {
        try {
            List<Folder> folderData = Ebean.find(Folder.class).where().
                    eq("folder_type", folderType).and().eq("email", emailAddress).findList();
            System.out.println(folderData.size());
            return getEmailItemsFromMails(folderData, emailAddress);

        } catch (Exception e) {
            System.out.println("Error occurred while fetching the data: "+e);
        }
        return null;
    }

    public ArrayList<EmailItem> getAllEmailDataForUser(String emailAdress) {
        List<Folder> folderData = Ebean.find(Folder.class).where().
                in("folder_type", Arrays.asList("inbox", "sent")).and().eq("email", emailAdress).findList();
        return getEmailItemsFromMails(folderData, emailAdress);
    }

    private ArrayList<EmailItem> getEmailItemsFromMails(List<Folder> folderItems, String emailAddress) {
        List<Integer> mailIds = new ArrayList<>();
        Map<Integer, Folder> mailIdToFolderMap = new HashMap<>();
        folderItems.forEach(data -> {mailIds.add(data.mail_id);mailIdToFolderMap.put(data.mail_id, data);});
        System.out.println(mailIds.get(0));
        List<Mails> mails = null;
        try {
            mails = Ebean.find(Mails.class).where().in("id", mailIds).findList();
            System.out.println(mails.get(0).body);
        } catch (Exception e) {
            System.out.println("Error occurred while finding the mail body: "+e);
        }

        ArrayList<EmailItem> items = new ArrayList<EmailItem>();

        for(Mails mail : mails) {
            EmailItem item = mail.getVOfromModel();
            Folder currFolder = mailIdToFolderMap.get(mail.id);
            item.userEmailAddress = emailAddress;
            item.fromUser = currFolder.from_user;
            item.currentFolder = currFolder.folder_type;
            items.add(item);
        }
        System.out.println(items.size());
        return items;
    }

}
