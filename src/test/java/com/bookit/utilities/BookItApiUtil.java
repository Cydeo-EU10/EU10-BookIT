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


    public static void deleteStudent(String studentEmail,String studentPassword){

        //1.send a get request to get token with student information
        String studentToken = BookItApiUtil.generateToken(studentEmail,studentPassword);

        //2.send a get request to /api/users/me endpoint and get the id number
        int idToDelete = given().accept(ContentType.JSON)
                .and().header("Authorization", studentToken)
                .when()
                .get(ConfigurationReader.get("qa2api.url") + "/api/users/me")
                .then().statusCode(200).extract().jsonPath().getInt("id");

        //3.send a delete request as a teacher to /api/students/{id} endpoint to delete the student
        String teacherToken =BookItApiUtil.generateToken(ConfigurationReader.get("teacher_email"),ConfigurationReader.get("teacher_password"));
        given().
                pathParam("id",idToDelete)
                .and().
                header("Authorization",teacherToken)
                .when()
                .delete(ConfigurationReader.get("qa2api.url")+"/api/students/{id}")
                .then()
                .statusCode(204);
    }
}
