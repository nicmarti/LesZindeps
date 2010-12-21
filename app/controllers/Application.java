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


    /**
     * Affiche le formulaire pour soumettre une mission.
     * Si nous avons besoin d'un captcha, il faudra mettre le code ici.
     */
    public static void mission() {
        render();
    }

    /**
     * Enregistre une demande de mission
     * @param propal est la nouvelle mission
     */
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

    /**
     * Recherche par nom et par prénom
     * @param s est le critere de recherche.
     */
    public static void search(String s){
        if(s==null){
            qui();
        }
        if(s.trim().equals("")){
            qui();
        }
        List<Zindep> listOfZindeps = Zindep.findByLastNameLike(s);
        renderTemplate("Application/qui.html",listOfZindeps);
    }

    /**
     * Affiche un profil, notez aussi dans le fichier "routes" comment l'URL elle est belle...
     * CE qui permettra de la mettre dans son CV par exemple (idée de David Dewalle)
     * @param id est vraiment utilisé pour charger la fiche
     * @param firstName ne sert pas mais permet de creer une URL propre dans routes
     * @param lastName ne sert pas mais permet de creer une URL propre dans routes
     *
     */
    public static void showProfile(String id,String firstName,String lastName){
        Zindep zindep=Zindep.findById(id);
        if(zindep==null){
            flash.error("Profil non trouvé ou désactivé");
            qui();
        }
        render(zindep);

    }


}