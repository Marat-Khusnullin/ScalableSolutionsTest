import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class ApiCalls {

    private final static String BASE_URL = "http://94.130.158.237:43587/api/order";

    public static Response createOrder(Order order) {
        Response response = given().contentType(ContentType.JSON).body(order)
                .when()
                .post(BASE_URL + "/create");
        return response;
    }

    public static Response createOrder(String json) {
        Response response = given().contentType(ContentType.JSON).body(json)
                .when()
                .post(BASE_URL + "/create");
        return response;
    }

    public static Response deleteOrderById(String id) {
        Response response = given()
                .queryParam("id", id)
                .when().delete(BASE_URL);
        return response;
    }

    public static Response getOrderById(String id) {
        Response response = given()
                .queryParam("id", id)
                .get(BASE_URL);
        return response;
    }

    public static Response cleanOrderbook() {
        Response response = given()
                .when().get(BASE_URL + "/clean");
        return response;
    }

    public static Response getMarketDataSnapshot() {
        Response response = given()
                .when().get("http://94.130.158.237:43587/api" + "/marketdata");
        return response;
    }
}
