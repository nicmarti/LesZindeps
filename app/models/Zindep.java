package models;

import net.sf.oval.constraint.Max;
import org.hibernate.annotations.GenericGenerator;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.libs.Codec;
import play.templates.JavaExtensions;

import javax.persistence.*;
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

    @Required(message = "Ce champ est obligatoire")
    @MaxSize(255)
    public String firstName;

    @Required(message = "Ce champ est obligatoire")
    @MaxSize(255)
    public String lastName;

    // Depuis quand tu fais partie du groupe ?
    public Date memberSince;

    @Required(message = "Ce champ est obligatoire")
    @Lob
    public String location;

    public String gravatarId;


    @Lob
    // Rename to avoir mysql being lost
    @Column(name = "zindep_index")
    public String index;


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


    public static List<Zindep> findByLastNameLike(String s) {
        if (s == null) {
            return findAllByName();
        }
        if (s.trim().equals("")) {
            return findAllByName();
        }

        return find("from Zindep z where z.index like ? order by z.lastName", JavaExtensions.noAccents("%" + s.toLowerCase() + "%")).fetch();

    }


    /**
     * Creation d'un index lors de la modification de l'entité et création du champ gravatar
     */
    @PreUpdate
    @PrePersist
    void index() {
        this.index = JavaExtensions.noAccents(this.firstName).toLowerCase() + " ";
        this.index += JavaExtensions.noAccents(this.lastName).toLowerCase() + " ";


         if (email != null) {
            // Gravatar
            this.gravatarId = Codec.hexMD5(email.trim().toLowerCase());
        }
    }
}
