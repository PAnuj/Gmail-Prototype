package models;
import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class User extends Model {
    @Id
    public Integer id;
    public String Name;
    public String Email;
    public String Geolocation;
    public String Domain;

    public static final Finder<Long, User> find = new Finder<>(User.class);
}
