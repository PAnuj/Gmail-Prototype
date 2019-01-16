package models;

import io.ebean.Finder;
import io.ebean.Model;
import view_objects.EmailItem;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Mails extends Model {

    @Id
    public Integer id;
    public String body;
    public Integer thread_id;
    public Integer time_stamp;
    public Integer attachmentId;

    public static final Finder<Long, Mails> find = new Finder<>(Mails.class);

    public EmailItem getVOfromModel() {
        EmailItem item = new EmailItem();
        item.emailBody = this.body;
        item.threadId = this.thread_id;
        item.timeStamp = this.time_stamp;
        item.userEmailId = this.id;
        return item;
    }
}
