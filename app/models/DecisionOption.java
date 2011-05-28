package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ToStringBuilder;

import play.db.jpa.Model;

@Entity
public class DecisionOption  extends Model{
	
    @ManyToOne
	public Decision decision;
	
	public String libelle;

	public DecisionOption(Decision decision, String libelle) {
		this.decision = decision;
		this.libelle = libelle;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	
	
	
}
