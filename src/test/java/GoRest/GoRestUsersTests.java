package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    int userId;
    User newUser;

    @BeforeClass
    void Setup() {

        baseURI = "https://gorest.co.in/public/v2/users";
    }

    @Test(enabled = false)
    public void createUserObject() {
        //baslangic  islemleri
        //token aldim
        //users jsonu hazirladim
        userId =
                given()
                        .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                        .contentType(ContentType.JSON)
                        .body("{\n" +
                                "    \"name\": \"dshg\",\n" +
                                "    \"gender\": \"male\",\n" +
                                "    \"email\": \"sdfsd@gmail.com\",\n" +
                                "    \"status\": \"active\"\n" +
                                "}")
                        // ust taraf request ozellikleridir yani hazirlik islemleri POSTMAN deki Authorization ve request body kismi
                        .log().uri()
                        .log().body()
                        .when() // requestin oldugu nokta
                        .post("") // responsun olustugu nokta create islemi post methodu ile cagiriyoruz postmandeki gibi
                        // ici bos olma sebebi su baseuridekini ustune ekliyor yani "users" da yazabilirdin ve http ile baslayan bisi yazsaydin aa yeni yazmis diyip o icindekini kullanirdi
                        //alt taraf response sonrasi postmandeki test oenceresi
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

        System.out.println("userId = " + userId);


    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomEmail() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@gmail.com";
    }

    @Test(enabled = false)
    public void createRandomUserObject() {
        userId =
                given()
                        .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"" + getRandomName() + "\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")
                        // ust taraf request ozellikleridir yani hazirlik islemleri POSTMAN deki Authorization ve request body kismi
                        .log().uri()
                        .log().body()
                        .when() // requestin oldugu nokta
                        .post("") // responsun olustugu nokta create islemi post methodu ile cagiriyoruz postmandeki gibi
                        // ici bos olma sebebi su baseuridekini ustune ekliyor yani "users" da yazabilirdin ve http ile baslayan bisi yazsaydin aa yeni yazmis diyip o icindekini kullanirdi
                        //alt taraf response sonrasi postmandeki test oenceresi
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

        System.out.println("userId = " + userId);
    }


    @Test(enabled = false)
    public void createRandomUserObjectWithMap() {

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", getRandomName());
        newUser.put("gender", "male");
        newUser.put("email", getRandomEmail());
        newUser.put("status", "active");

        userId =
                given()
                        .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id"); //extract burda id yi cekmemize yariyor yani biz userIdyi almak istiyorduk bir int donmesi gerekiyordu onu sagladi

        System.out.println("userId = " + userId);
    }

    @Test(enabled = false)
    public void createUserObjectWithObject() {

        newUser = new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userId =
                given()
                        .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id"); //extract burda id yi cekmemize yariyor yani biz userIdyi almak istiyorduk bir int donmesi gerekiyordu onu sagladi

        System.out.println("userId = " + userId);
    }

    @Test
    public void createUserObjectWithObjectJsonPath() {

        newUser = new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userId =
                given()
                        .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        .log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("id");

        System.out.println("userId = " + userId);

        // path : class ya da tip donusumune imkan vermeyen direkt veriyi verir. List<String> gibi
        // jsonPath : class donusumune ve tip donusumune izin vererek veriyi istedigimiz formata getirir.
    }

    @Test(dependsOnMethods = "createUserObjectWithObjectJsonPath",priority = 1)
    public void getUserByID() {

        given()
                .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                .pathParam("varUserID", userId)
                .log().uri()

                .when()
                .get("/{varUserID}")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userId));
        System.out.println("userId = " + userId);

        // path : class ya da tip donusumune imkan vermeyen direkt veriyi verir. List<String> gibi
        // jsonPath : class donusumune ve tip donusumune izin vererek veriyi istedigimiz formata getirir.
    }

    @Test(dependsOnMethods = "createUserObjectWithObjectJsonPath",priority = 2)
    public void updateUserObject() {

        newUser.setName("brokoli suyu");

//        2.yol
//       Map<String,String> userUpdate=new HashMap<>();
//       userUpdate.put("name","brokoli suyu");

        given()
                .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                .pathParam("varUserID", userId)
                .contentType(ContentType.JSON)
                .body(newUser) // bunu yapma sebebimiz postmande request bodye de yazmamiz gerekiyor burda onu yaptik
                // .body(userUpdate) // bunu mapli yontem icin yaptik
                // new user yukarda set edildi burda da postmandeki request bodye yazdik
                .log().body()
                .log().uri()

                .when()
                .put("/{varUserID}")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userId))
                .body("name", equalTo("brokoli suyu")); // bu da isim guncellendi mi diye kontrol
        System.out.println("userId = " + userId);
    }
    @Test(dependsOnMethods = "updateUserObject",priority = 3)
    public void deleteUserById() {


        given()
                .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                .pathParam("varUserID", userId)

                .log().uri()

                .when()
                .delete("/{varUserID}")

                .then()
                .log().body()
                .statusCode(204);
    }
    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative() {


        given()
                .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")
                .pathParam("varUserID", userId)

                .log().uri()

                .when()
                .delete("/{varUserID}")

                .then()
                .log().body()
                .statusCode(404)
                //.body("message",equalTo("Resource not found")) 2. yol
        ;
    }

    @Test
    public void getUsers(){
        Response body=
        given()
                .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647") //get icin gerek yok token yazmaya ama ben kendi sonradan eklediklerimi gormek icin yazdim

                .when()
                .get()

                .then()
                .log().body()
                .statusCode(200)
                .extract().response()
                ;

        //path ve json path farki
        int idUser3path = body.path("[2].id"); // tip donusumu otomatik yani uygun tip verilmeli
        int idUser3JsonPath = body.jsonPath().getInt("[2].id"); // tip donusumu kendi icinde yapilabiliyor
        System.out.println("idUser3path = " + idUser3path);
        System.out.println("idUser3JsonPath = " + idUser3JsonPath);

        User[] users=body.as(User[].class);
        System.out.println("Arrays.toString(users) = " + Arrays.toString(users));

        List<User> listUser=body.jsonPath().getList("", User.class); //jsonPath ile liste donusturerek alabiliyorum
        System.out.println("listUser = " + listUser);
    }

    @Test
    public void getUsersV1() {
        Response body =
                given()
                        .header("Authorization", "Bearer 1384425d294ed07d0b56b3f3c7a4986ee5e7ad6e79599df18c7cc9d448f94647")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();

        // body.as(), extract.as // tüm gelen response uygun nesneler için tüm classların yapılması gerekiyor.

        List<User> dataUsers= body.jsonPath().getList("data", User.class);
        // JSONPATH bir response içindeki bir parçayı nesneye ödnüştürebiliriz.
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

    }

}

class User {
    private int id;
    private String name;
    private String gender;
    private String email;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
