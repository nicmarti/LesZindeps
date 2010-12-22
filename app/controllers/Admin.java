package controllers;

import models.Zindep;
import play.libs.OpenID;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

/**
 * Ce controleur permet à chaque Zindep de gérer son profil librement.
 * Une bonne partie du code vient en fait de l'eXpress-Board.
 *
 * @author Nicolas Martignole
 * @since 22 déc. 2010 21:48:49
 */
public class Admin extends Controller {
    // Protege toutes les methodes sauf index et authentification via Google
    @Before(unless = {"index", "authenticateOpenIdGoogle"})
    static void checkLogin() {
        if (!session.contains("zindepId")) {
            flash.error("Merci de vous authentifier pour accéder à cette partie.");
            index();
        }
    }

    /**
     * Affiche la page d'accueil
     */
    public static void index() {
        render();
    }

    /**
     * Réalise l'authentification
     */
    public static void authenticateOpenIdGoogle() {
        if (OpenID.isAuthenticationResponse()) {
            OpenID.UserInfo verifiedUser = OpenID.getVerifiedID();
            if (verifiedUser == null) {
                flash.error("Erreur OpenID");
                index();
            }

            String userEmail = verifiedUser.extensions.get("email");
            if (userEmail == null) {
                flash.error("L'identification de votre compte sur le site des Zindeps s'effectue avec votre email." +
                        " Vous devez authoriser le domaine leszindeps.fr à accéder à votre email pour vous authentifier."
                );
                index();
            }

            Zindep zindep = Zindep.findByMail(userEmail);
            if (zindep == null) {
                flash.error("Désolé votre compte n'existe pas. Demandez à l'équipe d'ajouter votre email "
                        + userEmail
                        + " pour pouvoir vous authentifier avec ce compte.");
                index();
            }
            session.put("zindepId", zindep.id);
            session.put("zindepEmail", zindep.email);

            flash.success("Bienvenue " + zindep.firstName);

            // Attention ne pas passer de parametre ici pr des raisons de securité
            showMyProfile();

        } else {
            // Verify the id
            if (!OpenID.id("https://www.google.com/accounts/o8/id").required("email", "http://axschema.org/contact/email").verify()) {
                flash.put("error", "Oops. Cannot contact google");
                index();
            }
        }
    }

    /**
     * Cette methode est appelé d'authenticateOpenIdGoogle mais elle peut
     * aussi etre appelée directement si je garde en bookmark cette ressource.
     * Play! Framework est completement sans etat, donc on cherche le zindepId
     * et on recharge l'entité
     */
    public static void showMyProfile() {
        String id = session.get("zindepId");
        if (id == null) {
            error("Probleme avec l'authentification");
        }

        Zindep zindep = Zindep.findById(id);
        notFoundIfNull(zindep);

        render(zindep); // cette variable zindep est celle utilisee dans la page HTML directement

    }
}
