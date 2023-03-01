import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.assertEquals;



public class GetOrderTests {

    // В данном тесте проверяем как и отсутствие ошибок при попытке получить данные по валидному айди,
    // так и соответствие получаемых данных ожидаемым значениям
    @Test(dataProvider = "validIds", dataProviderClass = TestClass.class)
    public void getOrderByValidId(String validId) {
        // Добавляем Order с валидным айди
        var testOrder = DataGenerators.createRandomOrder();
        testOrder.setId(validId);
        ApiCalls.createOrder(testOrder).then().statusCode(200);

        Response response = ApiCalls.getOrderById(validId);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), testOrder.getId());
        assertEquals(responseOrder.getPrice(), testOrder.getPrice());
        assertEquals(responseOrder.getQuantity(), testOrder.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), testOrder.getSide().toLowerCase());
    }

    // Тест на проверку ошибки при попытке получить Order по ID, который не является Integer
    @Test(dataProvider = "nonIntegerIds", dataProviderClass = TestClass.class)
    public void getOrderByNonIntegerId(String nonIntegerId) {
        Response response = ApiCalls.getOrderById(nonIntegerId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке получить Order по ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds", dataProviderClass = TestClass.class)
    public void getOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        Response response = ApiCalls.getOrderById(lessOrEqualThanZeroId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке получить Order по ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds", dataProviderClass = TestClass.class)
    public void getOrderByMoreOrEqualThanTenThousandId(String moreOrEqualThanTenThousandId) {
        Response response = ApiCalls.getOrderById(moreOrEqualThanTenThousandId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке получить Order по несуществующему ID
    @Test
    public void getOrderByNonExistentId() {
        // Предварительно вызываем удаление объекта, поиск которого будем производить
        String nonExistentId = new Random().nextInt(1, 10000) + "";
        ApiCalls.deleteOrderById(nonExistentId);

        Response response = ApiCalls.getOrderById(nonExistentId);
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.body().jsonPath().get("message"), "Order not found");
    }
}
