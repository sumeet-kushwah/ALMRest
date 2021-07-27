package tests;

import io.restassured.response.Response;

public interface testInterface {
    Boolean createTest();
    Response getTestByID(int ID);
    Boolean getTestPath();
    Boolean deleteTest();
    Boolean countTest();
    Boolean downloadAttachments();
    Boolean renameTest();
    Boolean updateTestFieldValues();
}
