package restassured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAssureImpl {

        String token;
        String id;

        @BeforeClass
        public void setup() {
                System.out.println("=== Sebelum Semua Test ===");
                RestAssured.baseURI = "https://whitesmokehouse.com";
        }

        @AfterClass
        public void teardown() {
                System.out.println("=== Setelah Semua Test ===");

                token = null;
                id = null;
        }

        @Test
        public void testRegister() {
                String requestBody = "{\n" +
                                "    \"email\": \"makurototo@yopmail.com\",\n" +
                                "    \"full_name\": \"Widya\",\n" +
                                "    \"department\": \"Finance\",\n" +
                                "    \"title\": \"Finance\",\n" +
                                "    \"password\": \"asdASD123!@#\"\n" +
                                "}";
                Response response = RestAssured.given()
                                .header("Content-Type", "application/json")
                                .body(requestBody)
                                .log().all()
                                .when()
                                .post("/webhook/employee/add");

                System.out.println("Status code: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody().asString());
                id = response.jsonPath().getString("[0].id");
                System.out.println("Extracted ID: " + id);
        }

        @Test(dependsOnMethods = "testRegister", priority = 1)
        public void testLogin() {
                String requestBody = "{\n" +
                                "    \"email\": \"makurototo@yopmail.com\",\n" +
                                "    \"password\": \"asdASD123!@#\"\n" +
                                "}";
                Response response = RestAssured.given()
                                .header("Content-Type", "application/json")
                                .body(requestBody)
                                .log().all()
                                .when()
                                .post("/webhook/employee/login");

                System.out.println("Status code: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody().asString());
                token = response.jsonPath().getString("[0].token");
                System.out.println("Extracted Token: " + token);
        }

        @Test(dependsOnMethods = "testLogin", priority = 2)
        public void testGetEmployee() {
                Response response = RestAssured.given()
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .log().all()
                                .when()
                                .get("/webhook/employee/get?id=" + id);

                System.out.println("Response: " + response.asPrettyString());

                assert response.statusCode() == 200 : "Expected status code 200 but got " + response.statusCode();
                assert response.jsonPath().getString("[0].email").equals("makurototo@yopmail.com")
                                : "Expected email makurototo@yopmail.com but got "
                                                + response.jsonPath().getString("[0].email");
                assert response.jsonPath().getString("[0].full_name").equals("Widya")
                                : "Expected full name Widya but got " + response.jsonPath().getString("[0].full_name");
                assert response.jsonPath().getString("[0].department").equals("Finance")
                                : "Expected department Finance but got "
                                                + response.jsonPath().getString("[0].department");
        }
}