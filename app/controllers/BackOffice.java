package controllers;

import models.Propal;
import models.Zindep;
import org.apache.commons.codec.digest.DigestUtils;
import play.data.validation.Valid;
import play.libs.Codec;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

/**
 * Un controleur different pour surveiller l'arrière-boutique.
 *
 * @author Nicolas Martignole
 * @since 21 déc. 2010 14:21:41
 */
public class BackOffice extends Controller {

    @Before(unless = {"login"})
    static void checkLogin() {
        if (!session.contains("zindepId")) {
            flash.error("Merci de vous authentifier pour accéder à cette partie.");
            Admin.index();
        }
    }

    /**
     * Affiche la page d'accueil
     */
    public static void index() {
        render();
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


        Zindep existing = Zindep.findByMail(zindep.email);
        if (existing != null) {
            flash.error("Attention, un compte avec cet email existe déjà.");
            render("@newZindep", zindep);
        }

        zindep.validateAndSave();
        flash.success("Nouvel indépendant enregistré");
        index();
    }

    public static void listZindeps() {
        List<Zindep> listOfZindeps = Zindep.findAllByName();
        render(listOfZindeps);
    }

}

