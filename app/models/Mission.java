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

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

/**
 * Correspond aux anciennes missions réalisées par les Zindeps
 */
@Entity
public class Mission extends Model {
    @ManyToOne
    public Zindep zindep;

    @Required
    public String title;

    @Required
    public String role;

    @Temporal(TemporalType.DATE)
    @Required
    public Date initialDate;

    @Temporal(TemporalType.DATE)
    @Required
    public Date endDate;

    @Required
    @Min(0)
    public Long clientPrice;

    @Min(0)
    public Long intermediaryPrice;

    @Required
    public String customer;

    public String intermediary;

    @Lob
    @MaxSize(10000)
    public String comment;

    @Required
    public String location;

    public String country;

    @Required(message = "Le champ expérience est obligatoire, merci d'indiquer un nombre d'années")
    public Long exp;


    /**
     * Retourne la liste des missions déjà effectuées par cet indep
     *
     * @return une liste de missions
     */
    public static List<Mission> findByZindep(Zindep zindep) {
        return Mission.find("from Mission where zindep = :zindep order by endDate").bind("zindep", zindep).fetch();
    }

    /**
     * Retourne la liste des intermédiares vers la vue pour l'autocompletion
     * dans la page addMision
     * @param term est le terme entré par l'utilisateur.
     * @return une liste qui matche la recherche afin d'afficher une dropdown list.
     */
    public static List<String> getListOfIntermediaries(String term) {
        String whereClause = "";
        if (term != null) {
            whereClause = "where UPPER(intermediary) like UPPER('%" + term + "%') ";
        }
        Query query = JPA.em().createQuery("select distinct intermediary from Mission m " + whereClause + " order by intermediary");
        return query.getResultList();
    }

    public static List<String> getListOfLocations(String term) {
        String whereClause = "";
        if (term != null) {
            whereClause = "where UPPER(location) like UPPER('%" + term + "%') ";
        }
        Query query = JPA.em().createQuery("select distinct location from Mission m " + whereClause + " order by location");
        return query.getResultList();
    }

    public static List<String> getListOfCustomers(String term) {
        String whereClause = "";
        if (term != null) {
            whereClause = "where UPPER(customer) like UPPER('%" + term + "%') ";
        }
        Query query = JPA.em().createQuery("select distinct customer from Mission m " + whereClause + " order by customer");
        return query.getResultList();
    }
}
