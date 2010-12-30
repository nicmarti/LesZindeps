/*
 * Copyright(c) 2010 Les Zindeps.
 *
 * The code source of this project is distributed
 * under the Affero GPL GNU AFFERO GENERAL PUBLIC LICENSE
 * Version 3, 19 November 2007
 *
 * This file is part of project LesZindeps. The source code is
 * hosted on GitHub. The initial project was launched by
 * Nicolas Martignole.
 *
 * LesZindeps is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LesZindeps is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * Please see COPYING.AGPL.txt for the full text license
 * or online http://www.gnu.org/licenses/agpl.html
 */

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

    @Temporal(TemporalType.DATE)
    public Date memberSince;

    @Required(message = "Ce champ est obligatoire")
    @Lob
    public String location;

    // Champ calcule et mis a jour automatiquement
    public String gravatarId;


    @Lob
    // permet d'indexer simplement
    @Column(name = "zindep_index")
    public String index;


    // De quoi raconter sa vie
    @Lob
    public String bio;

    // De quoi parler des technos
    @Lob
    public String techno;

    @Email
    @MaxSize(255)
    public String emailBackup;


    @Override
    public String toString() {
        return "Zindep {" +
                "id=" + id +
                ", name=" + firstName +
                ", lastName= " + lastName +
                "}";
    }

    /**
     * Retourne la liste trié par nom des Zindeps.
     *
     * @return une liste triée ou vide... si un jour tous les zindeps venait à disparaitre.
     */
    public static List<Zindep> findAllByName() {
        return Zindep.find("from Zindep order by lastName").fetch();
    }


    /**
     * Search by last name on the index field.
     * @param s is a search critieria
     * @return a list of Zindep or an empty list if nothing was found.
     */
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

    public static Zindep findByMail(String mail) {
        if (mail == null) {
            return null;
        }
        return Zindep.find("from Zindep z where email=:mail").bind("mail", mail.trim().toLowerCase()).first();
    }
}
