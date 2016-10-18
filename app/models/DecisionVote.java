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
package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class DecisionVote extends Model {

	@ManyToOne
	public Decision decision;

	@ManyToOne
	public Zindep zindep;

	@OneToOne
	public DecisionOption decisionOption;

	@Required
	public boolean decisionValue;

	public static List<Zindep> findAllVotantsByIdDecision(long id) {
		return find(
				"select distinct dv.zindep from DecisionVote dv where dv.decision.id=? ",
				id).fetch();
	}

	public static List<Long> voteParZindep(Zindep currentZindep) {
		return find(
				"select dv.decisionOption.id from DecisionVote dv where dv.decisionValue=? and zindep=?",
				true, currentZindep).fetch();
	}

	public static Map<Zindep, List<Long>> votesId(long id) {
		List<Zindep> zindeps = findAllVotantsByIdDecision(id);
		Map<Zindep, List<Long>> votesByZindep = new HashMap<Zindep, List<Long>>();
		for (Zindep z : zindeps) {
			votesByZindep.put(z, voteParZindep(z));
		}
		return votesByZindep;
	}

	public static DecisionVote findByDecisionIdAndOptionChoisieId(long decisionId, long optionChoisieId) {
		return find("decision.id = ? and decisionOption.id = ?", decisionId, optionChoisieId).first();
	}

}
