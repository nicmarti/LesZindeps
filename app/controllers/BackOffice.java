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

    public static void listZindeps() {
        List<Zindep> listOfZindeps = Zindep.findAllByName();
        render(listOfZindeps);
    }

    /**
     * Charge la fiche de l'indep spécifié
     *
     * @param id a editer
     */
    public static void updateProfile(String id) {
        Zindep zindep = Zindep.findById(id);
        render(zindep);
    }

    /**
     * Sauvegarde les modifications
     *
     * @param zindep est une sorte de DTO
     * @param idEdit permet de repasser l'id... hummm c'est pas top
     */
    public static void doUpdateProfile(@Valid Zindep zindep, String idEdit) {
        // Handle errors
        if (validation.hasErrors()) {
            render("@updateProfile", zindep);
        }
        Zindep existing = Zindep.findById(idEdit);
        if (existing == null) {
            flash.error("Utilisateur non trouvé");
            listZindeps();
        }

        // c'est pourri et je pense qu'il y a un moyen plus intelligent pour le faire
        existing.email = zindep.email;
        existing.lastName = zindep.lastName;
        existing.firstName = zindep.firstName;
        existing.memberSince = zindep.memberSince;
        existing.location = zindep.location;

        existing.save();

        flash.success("Mise à jour effectuée");
        index();
    }


    public static void prepareDelete(String id) {
        render(id);
    }

    public static void confirmDelete(String id){
        Zindep toDelete=Zindep.findById(id);
        if(toDelete==null){
            flash.error("Fiche non trouvée");
            listZindeps();
        }
        toDelete.delete();
        flash.success("Fiche effacée");
        listZindeps();

    }
}

