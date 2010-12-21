package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Date;
import java.util.List;

/**
 * Une propal est une proposition de mission.
 *
 * @author Nicolas Martignole
 * @since 21 déc. 2010 14:09:12
 */
@Entity
public class Propal extends Model {
    @Required(message = "Le titre est obligatoire")
    public String title;
    @Lob
    public String description;
    public String localisation;
    public String tjm;
    @Required(message="Comment vous contacter ?")
    public String contact;

    public Date creationDate;

    public static List<Propal> findAllByDate() {
        List<Propal> list=Propal.find("from Propal order by creationDate desc").fetch();
        return list;
    }
}
