import POJO.TaskPojo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.given;

public class Tasks {
    /*!
     !* Task 1
     !* create a request to https://jsonplaceholder.typicode.com/todos/2
     !* expect status 200
     !* Converting Into POJO
    ! */

    @Test
    public void task1() {
        TaskPojo tp =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(TaskPojo.class);
        System.out.println("tp = " + tp);
    }

    /*?
     ?* Task 2
     ?* create a request to https://httpstat.us/203
     ?* expect status 203
     ?* expect content type TEXT
     ?*/
    @Test
    public void task2() {

        given()

                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT);
    }

    /*
     * Task 3
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     * expect BODY to be equal to "203 Non-Authoritative Information"
     */

    @Test
    public void task3() {

        given()

                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"));

        String bodyText =
                //2.yontem
                given()
                        .when()
                        .get("https://httpstat.us/203")

                        .then()
                        .log().body()
                        .statusCode(203)
                        .contentType(ContentType.TEXT)
                        .extract().body().asString();
        Assert.assertTrue(bodyText.equalsIgnoreCase("203 Non-Authoritative Information"));
    }


    /*!
     !* Task 4
     !* create a request to https://jsonplaceholder.typicode.com/todos/2
     !* expect status 200
     !* expect content type JSON
     !* expect title in response body to be "quis ut nam facilis et officia qui"
     !*/

    @Test
    public void task4() {
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"));
    }

    /**
     * Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */
    @Test
    public void task5() {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false));


        //2. yontem

        boolean completed=
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed"); //extract ve testng assertion yontemi

        Assert.assertFalse(completed);
    }
}
