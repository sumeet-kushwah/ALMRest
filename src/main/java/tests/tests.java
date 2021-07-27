package tests;

import Login.login;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class tests implements testInterface {

    private final login Olog;

    public tests(login log)
    {
        this.Olog = log;
    }

    @Override
    public Boolean createTest() {

        return null;
    }

    @Override
    public Response getTestByID(int ID) {

        String endPoint =  Olog.getALM_URL() + "/rest/domains/" + Olog.getDomain() + "/projects/"+ Olog.getProject() +"/tests/" + ID;
        System.out.println(endPoint);
        ValidatableResponse apiResponse = given().contentType("application/json\r\n").cookies(Olog.getCookies()).when().get(endPoint).then();
        apiResponse.log().body();
        return apiResponse.extract().response();
    }

    @Override
    public Boolean getTestPath() {
        return null;
    }

    @Override
    public Boolean deleteTest() {
        return null;
    }

    @Override
    public Boolean countTest() {
        return null;
    }

    @Override
    public Boolean downloadAttachments() {
        return null;
    }

    @Override
    public Boolean renameTest() {
        return null;
    }

    @Override
    public Boolean updateTestFieldValues() {
        return null;
    }
}
