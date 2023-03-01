package tests;

import helpers.ApiCalls;
import helpers.DataGenerators;
import helpers.DataProvider;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo.MarketDataSnapshot;
import pojo.Order;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateOrderTests {

    @BeforeMethod
    public void cleanData(){
        ApiCalls.cleanOrderbook().then().statusCode(200);
    }

    // Тест на проверку создания нового заказа с валидными ID
    @Test(dataProvider = "validIds", dataProviderClass = DataProvider.class)
    public void createOrderWithValidId(String validId) {
        // Создаем заказ с валидным айди и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setId(validId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
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
        // Создаем заказ с валидным айди и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setId(null);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
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

    // Тест на проверку ошибки при попытке создать заказ с ID, который не является Integer
    @Test(dataProvider = "nonIntegerIds", dataProviderClass = DataProvider.class)
    public void createOrderWithNonIntegerId(String nonIntegerId) {
        // Создаем заказ с айди не Integer и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setId(nonIntegerId);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке создать заказ с ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds", dataProviderClass = DataProvider.class)
    public void createOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        // Создаем заказ с айди меньше или равным нулю и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setId(lessOrEqualThanZeroId);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать заказ с ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds", dataProviderClass = DataProvider.class)
    public void createOrderByMoreOrEqualThanTenThousandId(String moreOrEqualThanTenThousandId) {
        // Создаем заказ с айди больше или равным 10000 и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setId(moreOrEqualThanTenThousandId);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be more or equal than 10000");
    }


    // Тест на проверку создания заказа с корректным значением поля price
    @Test(dataProvider = "validPrices", dataProviderClass = DataProvider.class)
    public void createOrderWithValidPrice(Double validPrice) {
        // Создаем заказ с валидной ценой и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(validPrice);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
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

    // Тест на проверку создания заказа без поля price
    // Судя по документации, поле Price - опциональное. Но проверить сценарий создания заказа без этого поля не успел
    @Test()
    public void createOrderWithoutPrice() {
        // Создаем заказ без поля price и отправляем на сервер
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(null);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
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

    // Тест на проверку ошибки при попытке создать заказ с price, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandPrices", dataProviderClass = DataProvider.class)
    public void createOrderByMoreOrEqualThanTenThousandPrice(Double moreOrEqualThanTenThousandPrice) {
        // Создаем заказ с price больше или равным 10000
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(moreOrEqualThanTenThousandPrice);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке создать заказ с price, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroPrices", dataProviderClass = DataProvider.class)
    public void createOrderByLessOrEqualThanZeroPrice(Double lessOrEqualThanZeroPrice) {
        // Создаем заказ с price меньше или равным нулю
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(lessOrEqualThanZeroPrice);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать заказ с price, точность которой больше двух знаков после запятой
    @Test
    public void createOrderByThreeSignPrecisionPrice() {
        // Создаем заказ со значением price точностью 3 знака после запятой
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(0.001);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price: Incorrect number of decimal digits");
    }

    // Тест на проверку ошибки при попытке создать заказ с price, который не является Double
    // В данном тесте используется "чистый" жсон, т.к. делать Order с полями String показалось не совсем корректным
    @Test(dataProvider = "nonDoublePrices", dataProviderClass = DataProvider.class)
    public void createOrderWithNonDoublePrice(String nonDoublePrice) {
        // Создаем заказ со значением price, которое не является типом double
        Order order = DataGenerators.createRandomOrder();
        JSONObject orderJson = new JSONObject();
        orderJson.put("id", order.getId());
        orderJson.put("price", nonDoublePrice);
        orderJson.put("quantity", order.getQuantity());
        orderJson.put("side", order.getSide());

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(orderJson.toJSONString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "'Price' should be a double");
    }


    // Тест на проверку создания заказа с корректным значением поля quantity
    @Test(dataProvider = "validQuantities", dataProviderClass = DataProvider.class)
    public void createOrderWithValidQuantity(Long validQuantities) {
        // Создаем заказ с валидным значением поля quantity
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(validQuantities);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
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

    // Тест на проверку ошибки при попытке создать заказ с quantity, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandQuantities", dataProviderClass = DataProvider.class)
    public void createOrderByMoreOrEqualThanTenThousandQuantity(Long moreOrEqualThanTenThousandQuantities) {
        // Создаем заказ с quantity больше или равным 10000
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(moreOrEqualThanTenThousandQuantities);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Quantity can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке создать заказ с quantity, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroQuantities", dataProviderClass = DataProvider.class)
    public void createOrderByLessOrEqualThanZeroQuantity(Long lessOrEqualThanZeroQuantity) {
        // Создаем заказ с quantity меньше или равным 0
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(lessOrEqualThanZeroQuantity);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Quantity can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать заказ с quantity, который не является Long
    // В данном тесте используется "чистый" жсон, т.к. делать Order с полями String показалось не совсем корректным
    @Test(dataProvider = "nonLongQuantities", dataProviderClass = DataProvider.class)
    public void createOrderWithNonLongQuantity(String nonLongQuantity) {
        // Создаем заказ с quantity, который не является типом Long
        Order order = DataGenerators.createRandomOrder();
        JSONObject orderJson = new JSONObject();
        orderJson.put("id", order.getId());
        orderJson.put("price", order.getPrice());
        orderJson.put("quantity", nonLongQuantity);
        orderJson.put("side", order.getSide());

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(orderJson.toJSONString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "'Quantity' should be a long");
    }

    // Тест на проверку ошибки при попытке создать заказ без quantity
    // Судя по документации, поле Quantity - обязательное. Но проверить сценарий создания заказа без этого поля не успел
    @Test
    public void createOrderWithoutQuantity() {
        // Создаем заказ без поля quantity
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(null);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        // Тут нужно поменять сообщение на актуальное
        //assertEquals(response.body().jsonPath().get("message"), "'Quantity' tratata");
    }


    // Тест на проверку создания заказа с корректным значением поля side
    @Test(dataProvider = "validSides", dataProviderClass = DataProvider.class)
    public void createOrderWithValidQuantity(String validSide) {
        // Создаем заказ с валидным полем side
        Order order = DataGenerators.createRandomOrder();
        order.setSide(validSide);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 200);

        // Проверяем заказ, который вернулся в ответ на запрос
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

    // Тест на проверку ошибки при попытке создать заказ с невалидным значением поля side
    @Test(dataProvider = "nonValidSides", dataProviderClass = DataProvider.class)
    public void createOrderWithNonValidSide(String nonValidSide) {
        // Создаем заказ с невалидным полем side
        Order order = DataGenerators.createRandomOrder();
        order.setSide(nonValidSide);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "side: Incorrect value");
    }

    // Тест на проверку ошибки при попытке создать заказ без side
    // Судя по документации, поле Side - обязательное. Но проверить сценарий создания заказа без этого поля не успел
    @Test
    public void createOrderWithoutSide() {
        // Создаем заказ без поля side
        Order order = DataGenerators.createRandomOrder();
        order.setSide(null);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        // Тут нужно поменять сообщение на актуальное
        //assertEquals(response.body().jsonPath().get("message"), "'Side' tratata");
    }

    // Тест на проверку ошибки при создании заказа с уже существующим ID
    @Test
    public void createOrderWithExistingId() {
        // Создаем заказ и сохраняем значение ID
        Order order = DataGenerators.createRandomOrder();
        var id = order.getId();

        // Добавляем заказ на сервер
        ApiCalls.createOrder(order).then().statusCode(200);

        // Создаем заказ с раннее использованным ID
        order = DataGenerators.createRandomOrder();
        order.setId(id);

        // Проверяем, что при попытке создать заказ в ответ пришла ошибка
        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        // Тут нужно поменять сообщение на актуальное
        //assertEquals(response.body().jsonPath().get("message"), "Order with this id already exists");

        // Проверяем, что в marketData лежит только один заказ
        response = ApiCalls.getMarketDataSnapshot();
        assertEquals(response.getStatusCode(), 200);
        MarketDataSnapshot marketDataSnapshot = response.body().as(MarketDataSnapshot.class);
        assertEquals(marketDataSnapshot.getAsks().size() + marketDataSnapshot.getBids().size(), 1);
    }
}
