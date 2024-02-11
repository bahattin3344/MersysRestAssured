
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;

public class HumanPositions {
    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    String humanID = "";

    String rndHumanName = "";
    String rndHumanShortName = "";

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
        rndHumanName = randomUreteci.name().firstName();
        rndHumanShortName = randomUreteci.name().lastName();
        Map<String, String> newHumanPositions = new HashMap<>();
        newHumanPositions.put("name", rndHumanName);
        newHumanPositions.put("shortName", rndHumanShortName);
        newHumanPositions.put("tenantId", "646cb816433c0f46e7d44cb0");

        humanID =
                given()
                        .spec(reqSpec)
                        .body(newHumanPositions)
                        .when()
                        .post("/school-service/api/employee-position")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createHumanPositions")
    public void updateHumanPositions() {
        String newHumanName = "Updated Name" + randomUreteci.number().digits(3);
        Map<String, String> updHumanPositions = new HashMap<>();
        updHumanPositions.put("id", humanID);
        updHumanPositions.put("name", newHumanName);
        updHumanPositions.put("shortName", rndHumanShortName);
        updHumanPositions.put("tenantId", "646cb816433c0f46e7d44cb0");
        given()
                .spec(reqSpec)
                .body(updHumanPositions)
                .when()
                .put("/school-service/api/employee-position")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }

    @Test(dependsOnMethods = "updateHumanPositions")
    public void deleteHumanPositions() {

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/employee-position/" + humanID)
                .then()
                .log().body()
                .statusCode(204)


        ;

    }

}
