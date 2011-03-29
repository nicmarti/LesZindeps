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

import java.util.List;

import models.Mission;
import models.Zindep;
import play.data.validation.Valid;

import com.google.gson.GsonBuilder;

/**
 * Ce controleur permet à chaque Zindep de gérer les missions qu'il a déjà effectuées
 *
 * @author hlassiege
 */
public class AdminMissions extends Admin {

    /**
     * Affiche la liste des missions déjà effectuées par ce Zindep
     */
    public static void showMissions() {
        List<Mission> missions = Mission.findAll();
        render(missions);
    }

    /**
     * Affiche le formulaire d'ajout de mission
     */
    public static void addMission() {
        render();
    }

    /**
     * Affiche le formulaire d'ajout de mission en mode édition
     *
     * @param missionId
     */
    public static void updateMission(Long missionId) {
        Mission mission = Mission.findById(missionId);
        if (mission == null) {
            error("Mission non trouvée");
        }
        render("@addMission", mission);
    }
    
    /**
     * Affiche les détails d'une mission
     *
     * @param missionId
     */
    public static void showReport(String poste,String intermediary, String customer, String region) 
    {
        List values = Mission.findPriceByExperience(poste,intermediary, customer, region);
        List statistics = Mission.findStatistics(poste,intermediary, customer, region);
        
        GsonBuilder builder = new GsonBuilder();
        String json = builder.create().toJson(values);
        render(json,values,statistics);
    }


    /**
     * Supprime une mission et renvoie vers la liste des missions
     *
     * @param missionId
     */
    public static void delMission(Long missionId) {
        Mission mission = Mission.findById(missionId);
        if (mission == null) {
            error("Mission non trouvée");
        }
        String id = session.get("zindepId");
        Zindep zindep = Zindep.findById(id);
        if (zindep == null) {
            flash.error("Impossible de trouver le propriétaire");
        }
        if (!mission.zindep.equals(zindep)) {
            flash.error("Désolé, vous ne pouvez pas effacer les missions d'un autre");
            showMissions();
        }

        mission.delete();
        flash.success("Mission effacée");
        showMissions();
    }

    /**
     * Sauve ou met a jour une mission
     *
     * @param mission
     */
    public static void doAddMission(@Valid Mission mission) {
        String id = session.get("zindepId");
        if (id == null) {
            error("Probleme avec l'authentification");
        }

        Zindep zindep = Zindep.findById(id);
        if (zindep == null) {
            error("Zindep non trouvé");
        }

        // Validation
        if (validation.hasErrors()) {
            params.flash();
            render("@addMission", mission);
        }
        // dans le cas d'une mise à jour, l'id est non null
        if (mission.id != null) {
            if (!mission.zindep.equals(zindep)) {
                flash.error("Désolé, vous ne pouvez pas modifier les missions d'un autre");
                showMissions();
            }
            mission.save();
        }
        // sinon c'est un nouveau, on persiste
        else {
            mission.zindep = zindep;
            zindep.missions.add(mission);
            zindep.save();
            mission.save();
        }

        flash.success("Mise à jour effectuée");
        showMissions();
    }

    public static void listOfIntermediaries(String term) {
        List<String> intermediaries = Mission.getListOfIntermediaries(term);
        renderJSON(intermediaries);
    }

    public static void listOfCustomers(String term) {
        List<String> customers = Mission.getListOfCustomers(term);
        renderJSON(customers);
    }

    public static void listOfLocations(String term) {
        List<String> locations = Mission.getListOfLocations(term);
        renderJSON(locations);
    }


}
