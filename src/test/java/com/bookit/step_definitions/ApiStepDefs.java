package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookItApiUtil;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {
    String token;
    Response response;
    String emailGlobal;
    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

        token = BookItApiUtil.generateToken(email,password);
        emailGlobal = email;
    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {
        System.out.println("token = " + token);

        //send a GET request "/api/users/me" endpoint to get current user info

         response = given().accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .when()
                .get(ConfigurationReader.get("qa2api.url") + "/api/users/me");

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
        //verify status code matches with the feature file expected status code
        Assert.assertEquals(statusCode,response.statusCode());

    }

    @Then("the information about current user from api and database should match")
    public void theInformationAboutCurrentUserFromApiAndDatabaseShouldMatch() {
        System.out.println("we will compare database and api in this step");

        //get information from database
        //connection is from hooks and it will be ready
        String query = "select firstname,lastname,role from users\n" +
                "where email = '"+emailGlobal+"'";

        Map<String,Object> dbMap = DBUtils.getRowMap(query);
        System.out.println("dbMap = " + dbMap);
        //save db info into variables
        String expectedFirstName = (String) dbMap.get("firstname");
        String expectedLastName = (String) dbMap.get("lastname");
        String expectedRole = (String) dbMap.get("role");

        //get information from api
        JsonPath jsonPath = response.jsonPath();
        //save api info into variables
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //compare database vs api
        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);

    }

    @Then("UI,API and Database user information must be match")
    public void uiAPIAndDatabaseUserInformationMustBeMatch() {
        //get information from database
        //connection is from hooks and it will be ready
        String query = "select firstname,lastname,role from users\n" +
                "where email = '"+emailGlobal+"'";

        Map<String,Object> dbMap = DBUtils.getRowMap(query);
        System.out.println("dbMap = " + dbMap);
        //save db info into variables
        String expectedFirstName = (String) dbMap.get("firstname");
        String expectedLastName = (String) dbMap.get("lastname");
        String expectedRole = (String) dbMap.get("role");

        //get information from api
        JsonPath jsonPath = response.jsonPath();
        //save api info into variables
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //get information from UI
        SelfPage selfPage = new SelfPage();
        String actualUIName = selfPage.name.getText();
        String actualUIRole = selfPage.role.getText();

        System.out.println("actualUIName = " + actualUIName);
        System.out.println("actualUIRole = " + actualUIRole);

         //UI vs DB
        String expectedFullName = expectedFirstName+" "+expectedLastName;
        //verify ui fullname vs db fullname
        Assert.assertEquals(expectedFullName,actualUIName);
        Assert.assertEquals(expectedRole,actualUIRole);

        //UI vs API
        //Create a fullname for api

        String actualFullName = actualFirstName+" "+actualLastName;

        Assert.assertEquals(actualFullName,actualUIName);
        Assert.assertEquals(actualRole,actualUIRole);

    }

    @When("I send POST request to {string} endpoint with following information")
    public void i_send_POST_request_to_endpoint_with_following_information(String path, Map<String,String> studentInfo) {

        System.out.println("studentInfo = " + studentInfo);

        response = given().accept(ContentType.JSON)
                .queryParams(studentInfo)
                .and().header("Authorization",token)
                .log().all()
                .when()
                .post(ConfigurationReader.get("qa2api.url") + path);


    }

}
