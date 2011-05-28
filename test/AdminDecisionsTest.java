import java.util.Collection;import static com.google.common.collect.Maps.newHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import models.Decision;
import models.DecisionOption;

import org.junit.Test;

import play.test.UnitTest;
import controllers.AdminDecisions;
import static com.google.common.collect.Lists.newArrayList;


public class AdminDecisionsTest extends UnitTest {

	@Test
	public void shouldReturnListOnlyContainsOptionsNotEmpty() {
		Map<String, String> options = new HashMap<String, String>();
		options.put("option_1", "valeur 1");
		options.put("option_2", "valeur 2");
		options.put("anotherKey", "whatever");
		options.put("anotherKey", null);
		options.put("option_3", "");
		Decision decision = new Decision();
		List<DecisionOption> filtredOption = AdminDecisions.filtreMapByKeyToCollectionString(options, decision);
		Assert.assertTrue(filtredOption.size()==2);
		Assert.assertTrue(filtredOption.get(0).libelle.contains("valeur 1"));
	}

	@Test
	public void shouldReturnOrderedListOptions() {
		Map<String, String> options = newHashMap();
		options.put("option_2", "valeur 2");
		options.put("option_1", "valeur 1");
		options.put("option_3", "valeur 3");
		
		Decision decision = new Decision();
		List<DecisionOption> filtredOption = AdminDecisions.filtreMapByKeyToCollectionString(options, decision);
		//doit contenir une liste ordonnée par clé 
		Assert.assertEquals("valeur 1", filtredOption.get(0).libelle);
		Assert.assertEquals("valeur 2", filtredOption.get(1).libelle);
		Assert.assertEquals("valeur 3", filtredOption.get(2).libelle);
	}

}
