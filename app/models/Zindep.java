package models;

import org.hibernate.annotations.GenericGenerator;
import play.data.validation.Email;
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
    public String email;

    @Required
    public String firstName;

    @Required
    public String lastName;

    // Depuis quand tu fais partie du groupe ?
    public Date memberSince;

    @Required
    @Lob
    public String location;


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
