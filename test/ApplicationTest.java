import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testThatAdminPageIsProtected() {
        Response response = GET("/admin/showmyprofile");
        assertStatus(302, response);
    }


    @Test
    public void testThatBackofficePageIsProtected() {
        Response response = GET("/backoffice/index");
        assertStatus(302, response);
    }
}