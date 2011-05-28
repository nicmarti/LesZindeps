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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.filterEntries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Decision;
import models.DecisionOption;
import models.DecisionVote;
import models.Zindep;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class AdminDecisions extends Admin {

	public static void index() {
		List<Decision> decisions = Decision.findAll();
		render(decisions);
	}
	
	/**
	 * Affiche la decision courante pour laquelle on va voter
	 * @param id identifiant de la decision à afficher
	 */
	public static void voter(long id){
		Map<Zindep, List<Long>> votes=DecisionVote.votesId(id);
		Zindep currentZindep = getZindep();
		if (!votes.containsKey(currentZindep)){
			votes.put(currentZindep, null);
		}
		Decision decision = Decision.findById(id);
		if (null==decision)
			notFound();
		render(decision,votes, currentZindep);
	}
	
	
	public static void newDecision(){
		render();
	}
	
	public static void remove(long id){
		Decision.findById(id)._delete();
		flash.success("Décision supprimée");
		index();
	}

	public static void savePoll(Decision decision) {
		decision.initiator = getZindep();
		validation.valid(decision);
		if (validation.hasErrors()) {
			params.flash();
			render("@newDecision", decision);
		}
		decision.save();
		render("@faireUnChoix", decision);
	}

	/**
	 * copie les parametre http commençant par option et les mets dans une liste de DecisionOption
	 * @param map
	 * @param decision
	 * @return
	 */
	public static List<DecisionOption> filtreMapByKeyToCollectionString(Map<String, String> map, Decision decision) {
		Predicate<Entry<String, String>> keyStartWithOption = new Predicate<Entry<String, String>>() {
			@Override
			public boolean apply(Entry<String, String> o) {
				return  (o != null && o.getKey().startsWith("option_") && !o.getValue().equals("")) ? true : false;
			}
		};
		List<String> values = newArrayList(filterEntries(map,keyStartWithOption).values());
		List<DecisionOption> decisionOptions = newArrayList();
		Collections.sort(values, String.CASE_INSENSITIVE_ORDER);
		for(String s : values){
			decisionOptions.add(new DecisionOption(decision, s));
		}
		return decisionOptions;
	}

	
	public static void saveVote(long decisionId, long optionChoisieId, boolean optionChoisieValue){
		DecisionVote decisionVote =DecisionVote.findByDecisionIdAndOptionChoisieId(decisionId,optionChoisieId);
		if (null == decisionVote)
			decisionVote = new DecisionVote();
		decisionVote.zindep =getZindep();
		decisionVote.decision=Decision.findById(decisionId);
		decisionVote.decisionOption =DecisionOption.findById(optionChoisieId);
		decisionVote.decisionValue = optionChoisieValue;
		decisionVote.save();
	}
	
	public static void saveOptions(@Required long id) {
		Decision decision = Decision.findById(id);
		decision.options = filtreMapByKeyToCollectionString(params.allSimple(),decision);
		decision.save();
		flash.success("Le sondage a été créé avec succès");
		index();
	}

	private static Zindep getZindep() {
		String email = session.get("zindepEmail");
		return Zindep.findByMail(email);
	}

}
