import models.Zindep;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Le bootstrap est execute en mode dev au demarrage pour placer quelques utilisateurs en base.
 *
 * @author Nicolas Martignole
 */
@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if (Play.mode == Play.Mode.DEV) {
            if (Zindep.count() == 0) {
                Fixtures.load("test-datas.yml");
            }
        }
    }

}