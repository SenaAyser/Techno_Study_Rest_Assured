import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {
        given()
                // hazırlık işlemlerini yapacağız (token,send body, parametreler)
                .when()
                // link i ve metodu veriyoruz
                .then()
        //  assertion ve verileri ele alma extract
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
                .contentType(ContentType.JSON) // donen sonuc json tipinde mi NOT : bu siralama onemsiz
        ;
    }


    @Test
    public void checkCountryInResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
                .body("country", equalTo("United States")) //body.country == United States //.body("id","esitligini")
        ;
    }


    //pm                              RestAssured
    //body.country                    body("country",
    //body.'post code'                body("post code",
    //body.places[0].'place name'     body("places[0].'place name'")
    //body.places.'place name'        body("places.'place name'")   -> bütün place name leri verir
    @Test
    public void checkStateInResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
                .body("places[0].state", equalTo("California")) // birebir esit mi
        ;
    }

    @Test
    public void bodyJsonPathTest3() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
                .body("places.'place name'", hasItem("Dervişler K2öyü")) // verilen pathdeki liste bu itema sahip mi
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/90210")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
                .body("places'", hasSize(1)) // placein size 1 e esit mi
        ;
    }

    @Test
    public void combiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
                .body("places", hasSize(1))
                .body("places.state", hasItem("California")) // verilen pathdeki liste bu itema sahip mi
                .body("places[0].'place name'", equalTo("Beverly Hills")) // birebir esit mi

        ;
    }

    @Test
    public void pathParamTest() {
        given()
                .pathParam("Country", "us")
                .pathParam("ZipCode", 90210)
                .log().uri()// request link request URI:   http://api.zippopotam.us/us/90210

                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")

                .then()
                .log().body() // log().all() butun responsu gosterir
                .statusCode(200) // status controlu
        ;
    }

    // 90210 dan 90213 kadar test sonuçlarında places in size nın hepsinde 1 geldiğini test ediniz.
    @Test
    public void pathParamTest2() {
        for (int i = 90210; i <= 90213; i++) {


            given()
                    .pathParam("Country", "us")
                    .pathParam("ZipCode", i)
                    .log().uri()// request link request URI:   http://api.zippopotam.us/us/90210

                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipCode}")

                    .then()
                    .log().body() // log().all() butun responsu gosterir
                    .statusCode(200) // status controlu
            ;
        }
    }

    @Test
    public void queryParamTest() {
        for (int i = 90210; i <= 90213; i++) {


            given()
                    .param("page", 1) // ?page=1 seklinde linke ekleniyot
                    .log().uri() // request link request URI:   http://api.zippopotam.us/us/90210

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body() // log().all() butun responsu gosterir
                    .statusCode(200) // status controlu
                    .body("meta.pagination.page", equalTo(1))
            ;
        }
    }

    @Test
    public void queryParamTest2() {

        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int pageNo = 1; pageNo <= 10; pageNo++) {

            given()
                    .param("page", pageNo) // ?page=1 seklinde linke ekleniyot
                    .log().uri() // request link request URI:   http://api.zippopotam.us/us/90210

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body() // log().all() butun responsu gosterir
                    .statusCode(200) // status controlu
                    .body("meta.pagination.page", equalTo(pageNo))
            ;
        }
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    void Setup() {

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void requestResponseSpecification() {
        // https://gorest.co.in/public/v1/users?page=3
        given()
                .param("page", 1)
                .spec(requestSpecification)
                .when()
                .get("/users") // http olsaydi baseuri kullanamazdi
                .then()
                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpecification);
    }

    @Test
    public void extractingJsonPath() {
        //String placeName=given().when().get("http://api.zippopotam.us/us/90210%22).then().statusCode(200).extract().path(%22places[0].%27place name'");
        String placeName =
                given()
                        .when()
                        .get("https://api.zippopotam.us/us/90210").then()
                        .statusCode(200)
                        .extract().path("places[0].'place name'");
        // extract metodu ile given ile başlayan satır,
        // bir değer döndürür hale geldi, en sonda extract olmalı
        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt() {
        int limit =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        Assert.assertEquals(limit, 10, "test sonucu");
    }

    @Test
    public void extractingJsonPathList() {
        List<Integer> idler =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.id");
        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(4235));
    }

    @Test
    public void extractingJsonPathStringList() {
        List<String> nameler =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.name");
        System.out.println("nameler = " + nameler);
        // Assert.assertTrue(nameler.contains("Shubhaprada"));
    }

    @Test
    public void extractingJsonPathResponsALl() {
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().response(); //butun body alindi .log().body() gozuken body

        List<Integer> idler = response.path("data.id");
        List<String> isimler = response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("response = " + response.prettyPrint()); //body yazdirildi pretty saha guzel yazmaya yarar
        System.out.println("idler = " + idler);
        System.out.println("isimler = " + isimler);
        System.out.println("limit = " + limit);

    }
    @Test
    public void extractingJsonPOJO(){
        //POJO : Json objecti
        // plain  old java object
        // Ogrenci ogr = new Ogrenci();
        // ogr.id=1;
        // ogr.isin= "burak unver"
        // ogr.tek="05234134";
        // System.out.println("ogr = " + org.tel);

        Location yer=
        given()

                .when()
                .get("https://api.zippopotam.us/us/90210")
                        .then()
                        //.log().body();
                .extract().as(Location.class); //location sablonuna gore

        System.out.println("yer.getPostCode() = " + yer.getPostCode());
        System.out.println("yer.getPlaces().get(0).getPlaceName() = " + yer.getPlaces().get(0).getPlaceName());

  /*      JSON DEserialize  JSON -> Class
        class Location{
            String postCode;
            String country;
            String abbreviation;
            ArrayList<Place> places
        }


        class Place {
            String placeName;
            String longitude;
            String state;
            String stateAbbreviation;
            String latitude;
        }



        JSON serialize  Class yapısındaki bilgiyi JSON a dönüştürmek  Class -> JSON
        {
            "post code": "90210",
                "country": "United States",
                "country abbreviation": "US",
                "places": [
            {
                "place name": "Beverly Hills",
                    "longitude": "-118.4065",
                    "state": "California",
                    "state abbreviation": "CA",
                    "latitude": "34.0901"
            }
    ]
        }*/
    }
}
