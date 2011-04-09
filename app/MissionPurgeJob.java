import java.util.Collection;
import java.util.Date;
import java.util.List;

import models.Propal;
import notifiers.Mails;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import play.jobs.Job;
import play.jobs.On;

/** Fire at 12pm (noon) every day **/
@On("0 0 12 * * ?")
public class MissionPurgeJob extends Job
{
    public void doJob()
    {
        List<Propal> propals = Propal.findDeprecated();
        for (Propal deprecatedPropal : propals)
        {
            Mails.sendPropalDeletedMessage(deprecatedPropal,deprecatedPropal.contact);
            deprecatedPropal.delete();
        }
    }

}