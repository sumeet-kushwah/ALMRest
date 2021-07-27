package Login;

import io.restassured.http.Headers;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import io.restassured.http.Header;
import io.restassured.http.Cookies;
import io.restassured.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class login implements loginInterface{

    private Cookies cookies;
    private static ResourceBundle globalProperties;

    //These are the cookies set by ALM authentication
    private String LWSSO_COOKIE_KEY = "";
    private String XSRF_TOKEN = "";
    private String QCSession = "";
    private String ALM_USER = "";

    ////These are the login parameters
    private String ALM_URL = "";
    private String Domain = "";
    private String Project = "";
    private String ClientID = "";
    private String Secret = "";


    public Cookies getCookies() {
        return cookies;
    }

    public String getALM_USER() {
        return ALM_USER;
    }

    public String getXSRF_TOKEN() {
        return XSRF_TOKEN;
    }

    public String getQCSession() {
        return QCSession;
    }

    public String getALM_URL() {
        return ALM_URL;
    }

    public String getLWSSO_COOKIE_KEY() {
        return LWSSO_COOKIE_KEY;
    }

    public String getDomain() {
        return Domain;
    }

    public String getProject() {
        return Project;
    }

    public login()
    {
        readResources();
    }

    private void readResources()
    {
        globalProperties = ResourceBundle.getBundle("login");
        this.ALM_URL = globalProperties.getString("ALMURL");
        this.Domain = globalProperties.getString("Domain");
        this.Project = globalProperties.getString("Project");
        this.ClientID = globalProperties.getString("ClientID");
        this.Secret = globalProperties.getString("Secret");
    }

    @Override
    public Boolean LoginUsingAPIKey() throws Exception {

        String LoginCookie = "";

        Cookie OCookie;
        //Cookies OCookies;
        List<Cookie> CookieList = new ArrayList();

        if (!(this.ALM_URL.endsWith("qcbin"))) {
            System.out.println("ALM URL must end with qcbin");
            throw (new Exception("Malformed ALM URL"));
        }

        String endPoint = this.ALM_URL + "/rest/oauth2/login";
        String body = "{\n" +
                      "\"clientId\": \"" + ClientID + "\",\n" +
                      "\"secret\": \"" + Secret + "\" \n" +
                      "}\n";

        ValidatableResponse apiResponse = given().body(body).when().post(endPoint).then();

        if (apiResponse.extract().response().getStatusCode() != 200)
            throw (new Exception("ALM Login Failed" + apiResponse.log().body() + apiResponse.log().headers()));

        //Log the response header
        Headers allHeaders = apiResponse.extract().response().headers();

        for (Header header : allHeaders) {
            if (header.getValue().contains("LWSSO_COOKIE_KEY")) {
                LWSSO_COOKIE_KEY = header.getValue().split("=")[1];
                OCookie = new io.restassured.http.Cookie.Builder("LWSSO_COOKIE_KEY", LWSSO_COOKIE_KEY).build();
                CookieList.add(OCookie);
            }

            if (header.getValue().contains("XSRF-TOKEN")) {
                XSRF_TOKEN = header.getValue().split("=")[1];
                OCookie = new io.restassured.http.Cookie.Builder("XSRF-TOKEN", XSRF_TOKEN).build();
                CookieList.add(OCookie);
            }
            if (header.getValue().contains("QCSession")) {
                QCSession = header.getValue().split("=")[1];
                OCookie = new io.restassured.http.Cookie.Builder("QCSession", QCSession).build();
                CookieList.add(OCookie);
            }
            if (header.getValue().contains("ALM_USER")) {
                ALM_USER = header.getValue().split("=")[1];
                OCookie = new io.restassured.http.Cookie.Builder("ALM_USER", ALM_USER).build();
                CookieList.add(OCookie);
            }
        }

        if (CookieList.size() > 0) {
            cookies = new Cookies(CookieList);
            return true;
        }else
        {
            return false;
        }
    }

    @Override
    public Boolean logOut() {
        String endPoint = this.ALM_URL + "/api/authentication/sign-out";
        ValidatableResponse apiResponse = given().cookie("LWSSO_COOKIE_KEY", LWSSO_COOKIE_KEY).when().post(endPoint).then();

        if (apiResponse.extract().response().getStatusCode() == 200) {
            System.out.println("====================== Logout Headers ===========================");
            apiResponse.log().headers();

            return true;
        } else
            return false;
    }
}
