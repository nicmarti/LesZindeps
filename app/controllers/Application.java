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

package controllers;

import models.Propal;
import models.Zindep;
import notifiers.Mails;
import play.Logger;
import play.mvc.*;

import java.util.Date;
import java.util.List;


/**
 * Controleur principal qui comme son nom l'indique, controle.
 *
 * @author Nicolas Martignole
 */
public class Application extends Controller {

    /**
     * Page d'accueil du site.
     */
    public static void index() {
        render();
    }

    /**
     * Mais qui sont les Zindeps ?
     * Retourne la liste des zindeps
     */
    public static void qui() {
        List<Zindep> listOfZindeps = Zindep.findAllVisibleByName();

        render(listOfZindeps);
    }


    /**
     * Affiche le formulaire pour soumettre une mission.
     * Si nous avons besoin d'un captcha, il faudra mettre le code ici.
     */
    public static void mission() {
        Propal propal = new Propal();
        render(propal);
    }

    /**
     * Enregistre une demande de mission
     *
     * @param propal est la nouvelle mission
     */
    public static void submitMission(Propal propal) {
        propal.creationDate = new Date();
        if (propal.validateAndSave() == false) {
            flash.error("Erreur, impossible de créer votre demande, merci de corriger le formulaire");
            validation.keep();
            mission();
        }

        flash.success("Merci d'avoir proposé une mission.");
        render();
    }

    /**
     * Recherche par nom et par prénom
     *
     * @param s est le critere de recherche.
     */
    public static void search(String s) {
        if (s == null) {
            qui();
        }
        if (s.trim().equals("")) {
            qui();
        }
        List<Zindep> listOfZindeps = Zindep.findByLastNameLike(s);
        renderTemplate("Application/qui.html", listOfZindeps);
    }

    /**
     * Affiche un profil, notez aussi dans le fichier "routes" comment l'URL elle est belle...
     * CE qui permettra de la mettre dans son CV par exemple (idée de David Dewalle)
     *
     * @param id        est vraiment utilisé pour charger la fiche
     * @param firstName ne sert pas mais permet de creer une URL propre dans routes
     * @param lastName  ne sert pas mais permet de creer une URL propre dans routes
     */
    public static void showProfile(String id, String firstName, String lastName) {
        Zindep zindep = Zindep.findById(id);
        if (zindep == null) {
            flash.error("Profil non trouvé ou désactivé");
            qui();
        }
        render(zindep);
    }

    /**
     * Envoi un email. Mon dieu c'est tellement simple avec Play que je ne vais même pas mettre un commentaire.
     * Y"a qu'à regarder Mails.java et comprendre la magie...
     *
     * @param id      est l'id de l'indep à contacter
     * @param message est le message à envoyer
     */
    public static void sendMessage(String id, String message) {
        if (message == null) {
            flash.error("Votre message est vide");
            showProfile(id, "", "");
        }
        if (message.trim().isEmpty()) {
            flash.error("Votre message est vide, merci de corriger");
            showProfile(id, "", "");
        }
        Zindep zindep = Zindep.findById(id);
        if (zindep == null) {
            flash.error("Un probleme technique empêche l'envoi de message pour l'instant. Merci de retenter plus tard.");
            showProfile(id, "", "");
        }
        Mails.sendMessageToUser(message, zindep.email);
        if (zindep.emailBackup != null) {
            if (zindep.emailBackup.trim().equals("")) {
                // email invalide. J'en profite pour le nettoyer et sauver la fiche proprement
                zindep.emailBackup = null;
                zindep.save();
            } else {
                // Verifie l'email
                validation.email(zindep.emailBackup);
                // si le mail est valide alors envoie la copie
                if (!validation.hasErrors()) {
                    Mails.sendMessageToUser(message, zindep.emailBackup);
                }
            }
        }
        flash.success("Message envoyé");
        showProfile(id, "", "");
    }

}