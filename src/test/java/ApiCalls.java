import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiCalls {
    private final static String BASE_URL = "http://94.130.158.237:43587/api";
    private static RequestSpecification request = given().baseUri(BASE_URL);


    public static Response createOrder(Order order) {
        Response response = request.contentType(ContentType.JSON).body(order)
                .when().basePath("/order/create")
                .post();
        return response;
    }

    public static Response createOrder(String json) {
        Response response = request.contentType(ContentType.JSON).body(json)
                .when().basePath("/order/create")
                .post();
        return response;
    }

    public static Response deleteOrderById(String id) {
        Response response = request
                .queryParam("id", id)
                .when().basePath("/order")
                .delete();
        return response;
    }

    public static Response getOrderById(String id) {
        Response response = request
                .queryParam("id", id).when().basePath("/order")
                .get();
        return response;
    }

    public static Response cleanOrderbook() {
        Response response = request
                .when().basePath("/order/clean")
                .get();
        return response;
    }

    public static Response getMarketDataSnapshot() {
        Response response = request
                .when().basePath("/marketdata")
                .get();
        return response;
    }
}
