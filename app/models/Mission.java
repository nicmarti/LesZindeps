package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
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
    @Min(0)
    public Long clientPrice;
    
    @Min(0)
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
    
    public static List<String> getListOfIntermediaries(String term)
    {
        String whereClause = "";
        if (term != null)
        {
            whereClause = "where UPPER(intermediary) like UPPER('%"+term+"%') ";
        }
        Query query = JPA.em().createQuery("select distinct intermediary from Mission m "+whereClause+" order by intermediary");
        return query.getResultList();
    }
    
    public static List<String> getListOfLocations(String term)
    {
        String whereClause = "";
        if (term != null)
        {
            whereClause = "where UPPER(location) like UPPER('%"+term+"%') ";
        }
        Query query = JPA.em().createQuery("select distinct location from Mission m "+whereClause+" order by location");
        return query.getResultList();
    }
    
    public static List<String> getListOfCustomers(String term)
    {
        String whereClause = "";
        if (term != null)
        {
            whereClause = "where UPPER(customer) like UPPER('%"+term+"%') ";
        }
        Query query = JPA.em().createQuery("select distinct customer from Mission m "+whereClause+" order by customer");
        return query.getResultList();
    }
}
