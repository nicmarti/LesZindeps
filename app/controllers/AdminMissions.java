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

import play.data.validation.Valid;

import models.Mission;
import models.Zindep;

/**
 * Ce controleur permet à chaque Zindep de gérer les missions qu'il a déjà effectuées
 *
 */
public class AdminMissions extends Admin 
{
    
    /**
     * Affiche la liste des missions déjà effectuées par ce Zindep
     */
    public static void showMyMissions() {
        String id = session.get("zindepId");
        if (id == null) {
            error("Probleme avec l'authentification");
        }

        Zindep zindep = Zindep.findById(id);
        if (zindep == null) {
            error("Zindep non trouvé");
        }
        
        List<Mission> missions = Mission.findByZindep(zindep);

        render(zindep,missions);

    }
    
    /**
     * Affiche le formulaire d'ajout de mission
     */
    public static void addMission ()
    {
        render(); 
    }
    
    public static void updateMission  (Long missionId)
    {
        Mission mission = Mission.findById(missionId);
        if (mission == null) {
            error("Mission non trouvée");
        }
        
        render("@addMission", mission);
    }

    public static void delMission (Long missionId)
    {
        Mission mission = Mission.findById(missionId);
        if (mission == null) {
            error("Mission non trouvée");
        }
        
        mission.delete();
        
        showMyMissions();
    }

    public static void doAddMission (@Valid Mission mission)
    {
        String id = session.get("zindepId");
        if (id == null) {
            error("Probleme avec l'authentification");
        }
        
        Zindep zindep = Zindep.findById(id);
        if (zindep == null) {
            error("Zindep non trouvé");
        }

        // Handle errors
        if (validation.hasErrors()) 
        {
            params.flash();
            render("@addMission", mission);
        }
        
        if (mission.id != null)
        {
            mission.save();
        }
        else
        {
            
            mission.zindep = zindep;

            zindep.missions.add(mission);
            zindep.save();
        }
        
        flash.success("Mise à jour effectuée");
        showMyMissions();     
    }

}
