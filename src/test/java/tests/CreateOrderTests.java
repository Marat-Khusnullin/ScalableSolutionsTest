package tests;

import helpers.ApiCalls;
import helpers.DataGenerators;
import helpers.DataProvider;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import pojo.Order;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateOrderTests {

    // Тест на проверку создания нового заказа с валидными ID
    @Test(dataProvider = "validIds", dataProviderClass = DataProvider.class)
    public void createOrderWithValidId(String validId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(validId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), order.getId());
        assertEquals(responseOrder.getPrice(), order.getPrice());
        assertEquals(responseOrder.getQuantity(), order.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), order.getSide().toLowerCase());

        // Проверяем, что на сервер добавился заказ соответствующий ожидаемому (тому, что пришел в ответе на запрос добавления)
        response = ApiCalls.getOrderById(order.getId());
        assertEquals(response.getStatusCode(), 200);

        Order createdOrder = response.body().as(Order.class);
        assertEquals(createdOrder.getId(), responseOrder.getId());
        assertEquals(createdOrder.getPrice(), responseOrder.getPrice());
        assertEquals(createdOrder.getQuantity(), responseOrder.getQuantity());
        assertEquals(createdOrder.getSide().toLowerCase(), responseOrder.getSide().toLowerCase());
    }

    // Тест на проверку создания заказа без указания ID
    @Test
    public void createOrderWithoutId() {
        Order order = DataGenerators.createRandomOrder();
        order.setId(null);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertNotNull(responseOrder.getId());
        assertEquals(responseOrder.getPrice(), order.getPrice());
        assertEquals(responseOrder.getQuantity(), order.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), order.getSide().toLowerCase());

        // Проверяем, что на сервер добавился заказ соответствующий ожидаемому по ID из ответа
        response = ApiCalls.getOrderById(responseOrder.getId());
        assertEquals(response.getStatusCode(), 200);

        Order createdOrder = response.body().as(Order.class);
        assertEquals(createdOrder.getId(), responseOrder.getId());
        assertEquals(createdOrder.getPrice(), order.getPrice());
        assertEquals(createdOrder.getQuantity(), order.getQuantity());
        assertEquals(createdOrder.getSide().toLowerCase(), order.getSide().toLowerCase());
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с ID, который не является Integer
    @Test(dataProvider = "nonIntegerIds", dataProviderClass = DataProvider.class)
    public void createOrderWithNonIntegerId(String nonIntegerId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(nonIntegerId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds", dataProviderClass = DataProvider.class)
    public void createOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(lessOrEqualThanZeroId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds", dataProviderClass = DataProvider.class)
    public void createOrderByMoreOrEqualThanTenThousandId(String moreOrEqualThanTenThousandId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(moreOrEqualThanTenThousandId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be more or equal than 10000");
    }


    // Тест на проверку создания pojo.Order с корректным значением поля price
    @Test(dataProvider = "validPrices", dataProviderClass = DataProvider.class)
    public void createOrderWithValidPrice(Double validPrice) {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(validPrice);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), order.getId());
        assertEquals(responseOrder.getPrice(), order.getPrice());
        assertEquals(responseOrder.getQuantity(), order.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), order.getSide().toLowerCase());

        // Проверяем, что на сервер добавился заказ соответствующий ожидаемому (тому, что пришел в ответе на запрос добавления)
        response = ApiCalls.getOrderById(order.getId());
        assertEquals(response.getStatusCode(), 200);

        Order createdOrder = response.body().as(Order.class);
        assertEquals(createdOrder.getId(), responseOrder.getId());
        assertEquals(createdOrder.getPrice(), responseOrder.getPrice());
        assertEquals(createdOrder.getQuantity(), responseOrder.getQuantity());
        assertEquals(createdOrder.getSide().toLowerCase(), responseOrder.getSide().toLowerCase());
    }

    // Тест на проверку создания pojo.Order без поля price
    // Судя по документации, поле Price - опциональное. Но проверить сценарий создания заказа без этого поля не успел
    @Test()
    public void createOrderWithoutPrice() {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(null);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), order.getId());
        //assertEquals(responseOrder.getPrice(), order.getPrice());
        assertEquals(responseOrder.getQuantity(), order.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), order.getSide().toLowerCase());

        // Проверяем, что на сервер добавился заказ соответствующий ожидаемому (тому, что пришел в ответе на запрос добавления)
        response = ApiCalls.getOrderById(order.getId());
        assertEquals(response.getStatusCode(), 200);

        Order createdOrder = response.body().as(Order.class);
        assertEquals(createdOrder.getId(), responseOrder.getId());
        //assertEquals(createdOrder.getPrice(), responseOrder.getPrice());
        assertEquals(createdOrder.getQuantity(), responseOrder.getQuantity());
        assertEquals(createdOrder.getSide().toLowerCase(), responseOrder.getSide().toLowerCase());
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с price, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandPrices", dataProviderClass = DataProvider.class)
    public void createOrderByMoreOrEqualThanTenThousandPrice(Double moreOrEqualThanTenThousandPrice) {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(moreOrEqualThanTenThousandPrice);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с price, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroPrices", dataProviderClass = DataProvider.class)
    public void createOrderByLessOrEqualThanZeroPrice(Double lessOrEqualThanZeroPrice) {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(lessOrEqualThanZeroPrice);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с price, точность которой больше двух знаков после запятой
    @Test
    public void createOrderByThreeSignPrecisionPrice() {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(0.001);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price: Incorrect number of decimal digits");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с price, который не является Double
    @Test(dataProvider = "nonDoublePrices", dataProviderClass = DataProvider.class)
    public void createOrderWithNonDoublePrice(String nonDoublePrice) {
        Order order = DataGenerators.createRandomOrder();
        JSONObject orderJson = new JSONObject();
        orderJson.put("id", order.getId());
        orderJson.put("price", nonDoublePrice);
        orderJson.put("quantity", order.getQuantity());
        orderJson.put("side", order.getSide());

        Response response = ApiCalls.createOrder(orderJson.toJSONString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "'Price' should be a double");
    }


    // Тест на проверку создания pojo.Order с корректным значением поля quantity
    @Test(dataProvider = "validQuantities", dataProviderClass = DataProvider.class)
    public void createOrderWithValidQuantity(Long validQuantities) {
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(validQuantities);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), order.getId());
        assertEquals(responseOrder.getPrice(), order.getPrice());
        assertEquals(responseOrder.getQuantity(), order.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), order.getSide().toLowerCase());

        // Проверяем, что на сервер добавился заказ соответствующий ожидаемому (тому, что пришел в ответе на запрос добавления)
        response = ApiCalls.getOrderById(order.getId());
        assertEquals(response.getStatusCode(), 200);

        Order createdOrder = response.body().as(Order.class);
        assertEquals(createdOrder.getId(), responseOrder.getId());
        assertEquals(createdOrder.getPrice(), responseOrder.getPrice());
        assertEquals(createdOrder.getQuantity(), responseOrder.getQuantity());
        assertEquals(createdOrder.getSide().toLowerCase(), responseOrder.getSide().toLowerCase());
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с quantity, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandQuantities", dataProviderClass = DataProvider.class)
    public void createOrderByMoreOrEqualThanTenThousandQuantity(Long moreOrEqualThanTenThousandQuantities) {
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(moreOrEqualThanTenThousandQuantities);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Quantity can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с quantity, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroQuantities", dataProviderClass = DataProvider.class)
    public void createOrderByLessOrEqualThanZeroQuantity(Long lessOrEqualThanZeroQuantity) {
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(lessOrEqualThanZeroQuantity);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Quantity can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с quantity, который не является Long
    @Test(dataProvider = "nonLongQuantities", dataProviderClass = DataProvider.class)
    public void createOrderWithNonLongQuantity(String nonLongQuantity) {
        Order order = DataGenerators.createRandomOrder();
        JSONObject orderJson = new JSONObject();
        orderJson.put("id", order.getId());
        orderJson.put("price", order.getPrice());
        orderJson.put("quantity", nonLongQuantity);
        orderJson.put("side", order.getSide());

        Response response = ApiCalls.createOrder(orderJson.toJSONString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "'Quantity' should be a long");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order без quantity
    // Судя по документации, поле Quantity - обязательное. Но проверить сценарий создания заказа без этого поля не успел
    @Test
    public void createOrderWithoutQuantity() {
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(null);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        // Тут нужно поменять сообщение на актуальное
        //assertEquals(response.body().jsonPath().get("message"), "'Quantity' tratata");
    }


    // Тест на проверку создания pojo.Order с корректным значением поля side
    @Test(dataProvider = "validSides", dataProviderClass = DataProvider.class)
    public void createOrderWithValidQuantity(String validSide) {
        Order order = DataGenerators.createRandomOrder();
        order.setSide(validSide);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), order.getId());
        assertEquals(responseOrder.getPrice(), order.getPrice());
        assertEquals(responseOrder.getQuantity(), order.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), order.getSide().toLowerCase());

        // Проверяем, что на сервер добавился заказ соответствующий ожидаемому (тому, что пришел в ответе на запрос добавления)
        response = ApiCalls.getOrderById(order.getId());
        assertEquals(response.getStatusCode(), 200);

        Order createdOrder = response.body().as(Order.class);
        assertEquals(createdOrder.getId(), responseOrder.getId());
        assertEquals(createdOrder.getPrice(), responseOrder.getPrice());
        assertEquals(createdOrder.getQuantity(), responseOrder.getQuantity());
        assertEquals(createdOrder.getSide().toLowerCase(), responseOrder.getSide().toLowerCase());
    }

    // Тест на проверку ошибки при попытке создать pojo.Order с невалидным значением поля side
    @Test(dataProvider = "nonValidSides", dataProviderClass = DataProvider.class)
    public void createOrderWithNonValidSide(String nonValidSide) {
        Order order = DataGenerators.createRandomOrder();
        order.setSide(nonValidSide);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "side: Incorrect value");
    }

    // Тест на проверку ошибки при попытке создать pojo.Order без side
    // Судя по документации, поле Side - обязательное. Но проверить сценарий создания заказа без этого поля не успел
    @Test
    public void createOrderWithoutSide() {
        Order order = DataGenerators.createRandomOrder();
        order.setSide(null);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        // Тут нужно поменять сообщение на актуальное
        //assertEquals(response.body().jsonPath().get("message"), "'Side' tratata");
    }
}
