package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

/**
 * 
 * Correspond au anciennes missions réalisées par les Zindeps
 * 
 */
@Entity
public class Mission extends Model 
{
    @ManyToOne
    public Zindep zindep;
    
    @Required
    public String title;
    
    @Temporal(TemporalType.DATE)
    @Required
    public Date initialDate;
    
    @Temporal(TemporalType.DATE)
    @Required
    public Date endDate;
    
    @Required
    public Long clientPrice;
    
    public Long intermediaryPrice;
    
    @Required
    public String customer;
    
    public String intermediary;
    
    @Lob
    @MaxSize(10000)
    public String comment;
    
    @Required
    public String location;
    
    @Required
    public Long exp;
    
    
    /**
     * Retourne la liste des missions déjà effectuées par cet indep
     *
     * @return une liste de missions
     */
    public static List<Mission> findByZindep(Zindep zindep) {
        return Mission.find("from Mission where zindep = :zindep order by endDate").bind("zindep", zindep).fetch();
    }
    
}
