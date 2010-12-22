package controllers;

import models.Propal;
import models.Zindep;
import org.apache.commons.codec.digest.DigestUtils;
import play.data.validation.Valid;
import play.libs.Codec;
import play.mvc.Controller;

import java.util.List;

/**
 * Un controleur different pour surveiller l'arrière-boutique.
 *
 * @author Nicolas Martignole
 * @since 21 déc. 2010 14:21:41
 */
public class BackOffice extends Controller {

    public static void index() {
        render();
    }

    /**
     * Affiche la liste des propals.
     */
    public static void showPropals() {
        List<Propal> listOfPropals = Propal.findAllByDate();
        render(listOfPropals);
    }

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

        zindep.validateAndSave();
        flash.success("Nouvel indépendant enregistré");
        index();
    }

    /**
     * Retourne la liste des Zindeps.
     * Le pattern flat controller dit que tout le code métier doit etre fait par les Entities (au sens DDD
     * et pas JPA).
     */
    public static void listZindeps() {
        List<Zindep> listOfZindeps = Zindep.findAllByName();
        render(listOfZindeps);
    }

}

