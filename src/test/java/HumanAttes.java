
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





public class HumanAttes   {
    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    String humanID = "";

    String rndHumanName = "";


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
    public void createHumanAttes() {
        rndHumanName = randomUreteci.name().firstName();
        Map<String, String> newHumanAttes = new HashMap<>();
        newHumanAttes.put("name", rndHumanName);


        humanID =
                given()
                        .spec(reqSpec)
                        .body(newHumanAttes)
                        .when()
                        .post("/school-service/api/attestation")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }
    @Test(dependsOnMethods = "createHumanAttes")
    public void updateHumanAttes() {
        String newHumanName = "Updated Name" + randomUreteci.number().digits(3);
        Map<String, String> updHumanAttes = new HashMap<>();
        updHumanAttes.put("id", humanID);
        updHumanAttes.put("name", newHumanName);
        given()
                .spec(reqSpec)
                .body(updHumanAttes)
                .when()
                .put("/school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }
    @Test(dependsOnMethods = "updateHumanAttes")
    public void deleteHumanDelete() {

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/attestation/" + humanID)
                .then()
                .log().body()
                .statusCode(204)


        ;

    }



}
