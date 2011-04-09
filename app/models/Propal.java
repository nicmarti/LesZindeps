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

import play.data.validation.Email;
import play.data.validation.Min;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;

import net.sf.oval.constraint.Future;

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
    @Required(message = "Nous avons vraiment besoin d'une description pour répondre à votre demande")
    public String description;
    @Required(message="Veuillez indiquer le lieu d'exécution de la mission")
    public String localisation;
    public String tjm;
    
    @Email(message="Veuillez indiquer une adresse email valide")
    @Required(message="Veuillez nous fournir une adresse email")
    public String contact;
    public String phone;
    
    @Min(value=0,message="Vous ne pouvez pas préciser une validité inférieure à 0 jours")
    @Required(message="Veuillez préciser le nombre de jours de validité de votre demande. Vous pourrez le modifier par la suite")
    public Long nbDaysOfValidity;
    
    public Date creationDate;

    public static List<Propal> findAllByDate() {
        List<Propal> list=Propal.find("from Propal order by creationDate desc").fetch();
        return list;
    }
}
