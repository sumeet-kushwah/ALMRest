import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import Login.*;
import tests.tests;

import java.util.ResourceBundle;

public class logintest {

    String ClientID;
    String Secret;



    @BeforeSuite
    public void setUp() {


        ClientID = "";
        Secret = "";
    }

    @Test
    public void test() throws Exception {
        login log = new login();
        Boolean ALMCookie = log.LoginUsingAPIKey();
         if (ALMCookie)
         {
            System.out.println("login Successful");
            System.out.println(log.getLWSSO_COOKIE_KEY());
         }

         tests test = new tests(log);
         Response response = test.getTestByID(497);
         System.out.println(response.getBody());

        if (log.logOut())
        {
            System.out.println("Logout Successful");
        }else
        {
            System.out.println("Logout failed");
        }
    }
}
