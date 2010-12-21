package controllers;

import models.Propal;
import play.mvc.Controller;

import java.util.List;

/**
 * Un controleur different pour surveiller l'arrière-boutique.
 *
 * @author Nicolas Martignole
 * @since 21 déc. 2010 14:21:41
 */
public class BackOffice extends Controller {
    /**
     * Affiche la liste des propals.
     */
    public static void showPropals() {
        List<Propal> listOfPropals = Propal.findAllByDate();
        render(listOfPropals);
    }
}
