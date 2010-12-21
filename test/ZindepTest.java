import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class ZindepTest extends UnitTest {

    /**
     * Verifie que l'attribut gravatar est bien mis a jour lorsque l'on persiste l'entité
     */
    @Test
    public void testGravatar() {
        String expected = "09b788738dcb5d36dbd782db5ad66304";

        Zindep tested=new Zindep();
        tested.email="nicolas@touilleur-express.fr";

        tested.save();
        assertNotNull(tested.gravatarId);
        assertEquals(expected,tested.gravatarId);
    }

}
