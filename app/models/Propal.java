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

import org.joda.time.DateMidnight;
import play.data.validation.Email;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import net.sf.oval.constraint.Future;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * This is a job offer that has an expiration date.
 *
 * @author Nicolas Martignole
 * @since 21 déc. 2010 14:09:12
 */
@Entity
public class Propal extends Model {
    @Required(message = "Le titre est obligatoire")
    public String title;

    @Lob
    @Required(message = "Nous avons vraiment besoin d'une description pour répondre à votre demande")
    public String description;

    @Required(message = "Veuillez indiquer le lieu d'exécution de la mission")
    public String localisation;

    /* how much you charge a day ? */
    public String tjm;

    /* Who submited this proposal ? */
    @Email(message = "Veuillez indiquer une adresse email valide")
    @Required(message = "Veuillez nous fournir une adresse email")
    public String contact;

    public String phone;

    @Max(value = 365, message = "Avez-vous vraiment une visibilité à 1 an ?")
    @Min(value = 0, message = "Vous ne pouvez pas préciser une validité inférieure à 0 jours")
    @Required(message = "Veuillez préciser le nombre de jours de validité de votre demande.")
    public int nbDaysOfValidity = 30;

    /* When was the propal created */
    public Date creationDate;

    /* When should it expire ? */
    public Date expirationDate;


    @PreUpdate
    @PrePersist
    private void calculateExpirationDate() {
        if (creationDate == null) {
            // Set the creation Date to today for existing propals in DB
            creationDate = new DateMidnight().toDate();
        }
        DateMidnight dt = new DateMidnight(creationDate);
        this.expirationDate = dt.plusDays(nbDaysOfValidity).toDate();
    }

    /**
     * Returns all propals order by creationDate.
     *
     * @return a list of Propals.
     */
    public static List<Propal> findAllByDate() {
        List<Propal> list = Propal.find("from Propal order by creationDate desc").fetch();
        return list;
    }

    /**
     * Returns all Propals that have expired.
     *
     * @return a list of Propal.
     */
    public static List<Propal> findDeprecated() {
        List<Propal> deprecatedPropals = find("expirationDate < :pnow")
                .bind("pnow", new DateMidnight().toDate())
                .fetch();
        return deprecatedPropals;
    }

}
