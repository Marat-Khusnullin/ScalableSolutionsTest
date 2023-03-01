package tests;

import helpers.ApiCalls;
import helpers.DataGenerators;
import helpers.DataProvider;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo.Order;
import java.util.LinkedList;
import static org.testng.Assert.assertEquals;

public class DeleteOrderTests {

    @BeforeMethod
    public void cleanData(){
        ApiCalls.cleanOrderbook().then().statusCode(200);
    }

    // В данном тесте проверяем как и отсутствие ошибок при попытке удалить данные по валидному айди,
    // так и успешное удаление сущности
    @Test(dataProvider = "validIds", dataProviderClass = DataProvider.class)
    public void deleteOrderByValidId(String validId) {
        // Добавляем заказ с валидным ID, по которому будем производить удаление
        var testOrder = DataGenerators.createRandomOrder();
        testOrder.setId(validId);
        ApiCalls.createOrder(testOrder).then().statusCode(200);

        Response response = ApiCalls.deleteOrderById(validId);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
        Order responseOrder = response.body().as(Order.class);
        Assert.assertEquals(responseOrder.getId(), testOrder.getId());
        Assert.assertEquals(responseOrder.getPrice(), testOrder.getPrice());
        Assert.assertEquals(responseOrder.getQuantity(), testOrder.getQuantity());
        Assert.assertEquals(responseOrder.getSide().toLowerCase(), testOrder.getSide().toLowerCase());

        // Проверяем, что ранее удаленного заказа нет на сервере
        response = ApiCalls.getOrderById(validId);
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.body().jsonPath().get("message"), "pojo.Order not found");
    }

    // Тест на проверку ошибки при попытке удалить заказ по ID, который не является Integer
    @Test(dataProvider = "nonIntegerIds", dataProviderClass = DataProvider.class)
    public void deleteOrderByNonIntegerId(String nonIntegerId) {
        Response response = ApiCalls.deleteOrderById(nonIntegerId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке удалить заказ по ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds", dataProviderClass = DataProvider.class)
    public void deleteOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        Response response = ApiCalls.deleteOrderById(lessOrEqualThanZeroId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке удалить заказ по ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds", dataProviderClass = DataProvider.class)
    public void deleteOrderByMoreOrEqualThanTenThousandId(String moreOrEqualThanTenThousandId) {
        Response response = ApiCalls.deleteOrderById(moreOrEqualThanTenThousandId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке удалить заказ по несуществующему ID
    @Test
    public void deleteOrderByNonExistentId() {
        Response response = ApiCalls.deleteOrderById("1");
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.body().jsonPath().get("message"), "pojo.Order not found");
    }

    // Тест на проверку удаления всех записей
    @Test
    public void cleanOrderBook() {
        // Предварительно добавляем записи
        var listOfIds = new LinkedList<String>();
        for (int i = 0; i < 10; i++) {
            var order = DataGenerators.createRandomOrder();
            ApiCalls.createOrder(order).then().statusCode(200);
            listOfIds.add(order.getId());
        }

        Response response = ApiCalls.cleanOrderbook();
        assertEquals(response.getStatusCode(), 200);
        // Не уверен что тут сообщение с точкой. Не зафиксировал себе его при ручном прогоне тестов. Взял из задания
        assertEquals(response.body().jsonPath().get("message"), "pojo.Order book is clean.");

        // Проверяем, что каждый из добавленных раннее заказов удалился
        for (String listOfId : listOfIds) {
            response = ApiCalls.deleteOrderById(listOfId);
            assertEquals(response.getStatusCode(), 404);
            assertEquals(response.body().jsonPath().get("message"), "pojo.Order not found");
        }
    }
}
