package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Folder extends Model {

    @Id
    public Integer id;
    public String email;
    public String folder_type;
    public Integer mail_id;
    public boolean is_read;
    public Integer thread_id;
    public Integer time_stamp;
    public String from_user;

    public static final Finder<Long, models.Mails> find = new Finder<>(models.Mails.class);
}
