package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookItApiUtil {


    public static String generateToken(String email,String password){
        Response response = given().
                accept(ContentType.JSON)
                .queryParam("email", email)
                .queryParam("password", password)
                .when()
                .get(ConfigurationReader.get("qa2api.url") + "/sign");

        String token = response.path("accessToken");

        String finalToken ="Bearer "+token;

        return  finalToken;
    }


    //one method param role --> userType "student-member" "student-leader" "teacher"
    //returns --> token
}
