

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;


public class Fields {
    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    String fieldID = "";
    String rndFieldsName = "";
    String rndCode = "";


    @BeforeClass
    public void Setup() {
        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
        ;

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

    }
    @Test
    public void createHumanPositions() {
        String fieldType = "STRING";
        rndFieldsName = randomUreteci.name().firstName();
        rndCode = randomUreteci.currency().code();
        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", rndFieldsName);
        newFields.put("code", rndCode);
        newFields.put("schoolId", "6576fd8f8af7ce488ac69b89");
        newFields.put("type",fieldType);

        fieldID =
                given()
                        .spec(reqSpec)
                        .body(newFields)
                        .when()
                        .post("/school-service/api/entity-field")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }
    @Test
    public void deleteFields() {

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/entity-field/" + fieldID)
                .then()
                .log().body()
                .statusCode(204)
        ;
    }
}
