package jobs;
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

import models.Propal;
import models.Zindep;
import notifiers.Mails;
import play.data.validation.Validation;
import play.jobs.Job;
import play.jobs.On;

import java.util.List;
import java.util.logging.Logger;

/**
 * Fire at 12pm (noon) every day *
 */
@On("0 0 12 * * ?")
public class MissionPurgeJob extends Job {

    public void doJob() {
        List<Propal> propals = Propal.findDeprecated();
        for (Propal deprecatedPropal : propals) {
            Validation.ValidationResult result = Validation.email("Email invalide", deprecatedPropal.contact);
            if (result.ok) {
                Mails.sendPropalDeletedMessage(deprecatedPropal, deprecatedPropal.contact);
                List<Zindep> listOfVisibles = Zindep.findAllVisibleByName();
                for (Zindep z : listOfVisibles) {
                    Mails.sendDeprecatedPropalToZindep(deprecatedPropal, z.email);
                }
            }else{
                play.Logger.debug("Email invalide dans une Propal " + deprecatedPropal.contact);
            }
            deprecatedPropal.delete();
        }
    }

}