package restassured;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAssureImpl {
    // String token =
    // "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjIzOCIsImlhdCI6MTc0NzkwNDI5OX0.tDOJxWRsLWOUX0pOgyQk12XCuFrC7CoP2cMc3u5dxqU";

    String token;

    @Test
    public void testLogin() {
        RestAssured.baseURI = "https://whitesmokehouse.com";

        String requestBody = "{\n" +
                "    \"email\": \"afteroffice6@yopmail.com\",\n" +
                "    \"password\": \"afteroffice1234\"\n" +
                "}";
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("/webhook/employee/login");

        System.out.println("Response: " + response.jsonPath().getString("[0].token"));
        token = response.jsonPath().getString("[0].token");

    }

    @Test(dependsOnMethods = "testLogin", priority = 1)
    public void testGetEmployee() {
        RestAssured.baseURI = "https://whitesmokehouse.com";
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/employee/get");

        System.out.println("Response: " + response.asPrettyString());
        assert response.statusCode() == 200 : "status code is 200 but got" + response.statusCode();
        assert response.jsonPath().getString("[0].email").equals("afteroffice6@yopmail.com")
                : "expected email afteroffice6@yopmail.com " + response.jsonPath().getString("[0].email");
        assert response.jsonPath().getString("[0].full_name").equals("afteroffice1234")
                : "Expected full name afteroffice1234 but got " + response.jsonPath().getString("[0].full_name");

        assert response.jsonPath().getString("[0].department").equals("Manager")
                : "Expected department Manager but got " + response.jsonPath().getString("[0].department");
    }

    @Test(dependsOnMethods = "testLogin", priority = 2)
    public void testUpdateEmployee() {
        RestAssured.baseURI = "https://whitesmokehouse.com";

        String bodyUpdate = "{\n" +
                "  \"email\": \"afteroffice3@yopmail.com\",\n" +
                "  \"full_name\": \"afteroffice1234\",\n" +
                "  \"department\": \"Manager\",\n" +
                "  \"title\": \"Manager\",\n" +
                "  \"password\": \"afteroffice1234\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(bodyUpdate)
                .log().all()
                .when()
                .put("/webhook/employee/update");

        System.out.println("Response: " + response.asPrettyString());

    }

    @Test(dependsOnMethods = "testLogin", priority = 3)
    public void testDeleteEmployee() {
        RestAssured.baseURI = "https://whitesmokehouse.com";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .delete("/webhook/employee/delete");

        System.out.println("Response: " + response.asPrettyString());
    }

    @Test
    public void testGetAllEmployees() {
        RestAssured.baseURI = "https://whitesmokehouse.com";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("/webhook/employee/get_all");

        System.out.println("Response: " + response.asPrettyString());

        assert response.statusCode() == 200 : "Status code is 200 but got " + response.statusCode();
    }

    // @Test
    // public void testEmployeeInvalidToken() {
    // RestAssured.baseURI = "https://whitesmokehouse.com";

    // Response response = RestAssured.given()
    // .header("Content-Type", "application/json")
    // .header("Authorization", "Bearer " + token + "invalid")
    // .log().all()
    // .when()
    // .get("/webhook/employee/get");

    // // System.out.println("Response: " + response.asPrettyString());

    // // assert response.statusCode() == 403 : "Status code is 403 but got " +
    // response.statusCode();
    //     }

}
