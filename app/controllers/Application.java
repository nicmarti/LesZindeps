package controllers;

import models.Propal;
import models.Zindep;
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
        List<Zindep> listOfZindeps = Zindep.findAllByName();
        render(listOfZindeps);
    }


    public static void mission() {
        render();
    }

    public static void submitMission(Propal propal) {
        if (propal.validateAndSave() == false) {
            flash.error("Erreur, impossible de créer votre demande, merci de corriger le formulaire");
            validation.keep();
            mission();
        }
        propal.creationDate=new Date();
        propal.save();

        flash.success("Merci d'avoir proposé une mission.");
        render();
    }

}