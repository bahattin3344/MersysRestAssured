
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;


public class Document {
    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    String documentID = "";

    String rndDocumentName = "";

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
    public void createDocument() {
        List<String> attachmentStages = new ArrayList<>();
        attachmentStages.add("STUDENT_REGISTRATION");
         rndDocumentName=randomUreteci.name().firstName();
        Map<String, Object> newDocument = new HashMap<>();
        newDocument.put("name", rndDocumentName);
        newDocument.put("schoolId", "6576fd8f8af7ce488ac69b89");
        newDocument.put("attachmentStages", attachmentStages);


        documentID =
                given()
                        .spec(reqSpec)
                        .body(newDocument)
                        .when()
                        .post("/school-service/api/attachments/create")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }
    @Test
    public void deleteDocument() {

        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/attachments/" + documentID)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

}
