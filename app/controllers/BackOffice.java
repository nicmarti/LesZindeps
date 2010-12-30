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

import models.Zindep;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

/**
 * Un controleur different pour surveiller l'arrière-boutique.
 * Mon idée est que ce controller n'est pas indispensable. Il sert le temps de la mise en route du projet.
 * Ensuite, il faut que le reste des outils se suffisent à eux-mêmes.
 *
 * @author Nicolas Martignole
 * @since 21 déc. 2010 14:21:41
 */
public class BackOffice extends Controller {

    /**
     * Methode appelée à chaque fois pour vérifier si l'utilisateur est authentifié ou non.
     */
    @Before(unless = {"login"})
    static void checkLogin() {
        if (!session.contains("zindepId")) {
            flash.error("Merci de vous authentifier pour accéder à cette partie.");
            Admin.index();
        }
    }

    /**
     * Affiche la page d'accueil.
     */
    public static void index() {
        render();
    }

    /**
     * Affiche la page de création d'un indep.
     */
    public static void newZindep() {
        render();
    }

    /**
     * Valide et sauvegarde un nouvel indépendant
     *
     * @param zindep est le nouvel indépendant
     */
    public static void storeNewZindep(@Valid Zindep zindep) {
        // Handle errors
        if (validation.hasErrors()) {
            render("@newZindep", zindep);
        }


        Zindep existing = Zindep.findByMail(zindep.email);
        if (existing != null) {
            flash.error("Attention, un compte avec cet email existe déjà.");
            render("@newZindep", zindep);
        }

        zindep.validateAndSave();
        flash.success("Nouvel indépendant enregistré");
        index();
    }

    /**
     * Retourne la liste des zindeps, le tag listOfZindeps est directement itéré dans la page HTML
     * grace à Groovy.
     */
    public static void listZindeps() {
        List<Zindep> listOfZindeps = Zindep.findAllByName();
        render(listOfZindeps);
    }


    /**
     * Permet d'effacer un compte invalide
     * @param id
     */
    public static void prepareDelete(String id) {
        render(id);
    }

    /**
     * Effacement
     * @param id
     */
    public static void confirmDelete(String id) {
        Zindep toDelete = Zindep.findById(id);
        if (toDelete == null) {
            flash.error("Fiche non trouvée");
            listZindeps();
        }
        toDelete.delete();
        flash.success("Fiche effacée");
        listZindeps();

    }

}

