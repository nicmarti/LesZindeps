package models;

import net.sf.oval.constraint.Max;
import org.hibernate.annotations.GenericGenerator;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;
import java.util.List;

/**
 * Un Zindep comme son nom l'indique est une espece rare et protegee qui represente un independant.
 *
 * @author Nicolas Martignole
 * @since 20 déc. 2010 22:37:31
 */
@Entity
public class Zindep extends GenericModel {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String id;

    @Required(message = "Email est obligatoire")
    @Email
    @MaxSize(255)
    public String email;

    @Required(message="Ce champ est obligatoire")
    @MaxSize(255)
    public String firstName;

    @Required(message="Ce champ est obligatoire")
    @MaxSize(255)
    public String lastName;

    // Depuis quand tu fais partie du groupe ?
    public Date memberSince;

    @Required(message="Ce champ est obligatoire")
    @Lob
    public String location;
    
    public String gravatarId;


    @Override
    public String toString() {
        return "Zindep {" +
                "id='" + id + '\'' +
                ", name='" + firstName +
                " " + lastName +
                '}';
    }

    /**
     * Retourne la liste trié par nom des Zindeps.
     *
     * @return une liste triée ou vide... si un jour tous les zindeps venait à disparaitre.
     */
    public static List<Zindep> findAllByName() {
        return Zindep.find("from Zindep order by lastName").fetch();
    }
}
