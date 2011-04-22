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

package notifiers;

/**
 * Generates all emails for the application, using text plain templates and text/html templates.
 *
 *
 * @author Nicolas Martignole
 * @since 29 mars 2010 17:21:31
 */

import models.Propal;
import play.mvc.*;


public class Mails extends Mailer {

    public static void sendMessageToUser(String message, String email) {
        setSubject("Message envoyé via le site des zindeps");
        setFrom("contact@leszindeps.fr");
        addRecipient(email);
        send(message);
    }

    public static void sendPropalDeletedMessage(Propal deprecatedPropal, String contact) {
        setSubject("Malheureusement votre demande n'a pu être prise en compte (Message envoyé via le site des zindeps)");
        setFrom("contact@leszindeps.fr");
        addRecipient(contact);
        send(deprecatedPropal);
    }
}