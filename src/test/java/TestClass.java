import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class TestClass {
    private final String BASE_URL = "http://94.130.158.237:43587/api/order";

    @DataProvider(name = "validIds")
    public Object[][] provideValidIds() {
        return new Object[][]{{"1"}, {"50"}, {"9999"}};
    }

    @DataProvider(name = "nonIntegerIds")
    public Object[][] provideNonIntegerIds() {
        return new Object[][]{{"string"}, {"12345678909876323423"}, {"!*"}};
    }

    @DataProvider(name = "lessOrEqualThanZeroIds")
    public Object[][] provideLessOrEqualThanZeroIds() {
        return new Object[][]{{"0"}, {"-1"}, {"-100"}};
    }

    @DataProvider(name = "moreOrEqualThanTenThousandIds")
    public Object[][] provideMoreOrEqualThanTenThousandIds() {
        return new Object[][]{{"10000"}, {"10001"}, {"15000"}};
    }


    // В данном тесте проверяем как и отсутствие ошибок при попытке получить данные по валидному айди,
    // так и соответствие получаемых данных ожидаемым значениям
    @Test(dataProvider = "validIds")
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
    @Test(dataProvider = "nonIntegerIds")
    public void getOrderByNonIntegerId(String nonIntegerId) {
        Response response = ApiCalls.getOrderById(nonIntegerId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке получить Order по ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds")
    public void getOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        Response response = ApiCalls.getOrderById(lessOrEqualThanZeroId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке получить Order по ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds")
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


    // В данном тесте проверяем как и отсутствие ошибок при попытке удалить данные по валидному айди,
    // так и успешное удаление сущности
    @Test(dataProvider = "validIds")
    public void deleteOrderByValidId(String validId) {
        // Добавляем Order с валидным ID, по которому будем производить удаление
        var testOrder = DataGenerators.createRandomOrder();
        testOrder.setId(validId);
        ApiCalls.createOrder(testOrder).then().statusCode(200);

        Response response = ApiCalls.deleteOrderById(validId);
        assertEquals(response.getStatusCode(), 200);

        Order responseOrder = response.body().as(Order.class);
        assertEquals(responseOrder.getId(), testOrder.getId());
        assertEquals(responseOrder.getPrice(), testOrder.getPrice());
        assertEquals(responseOrder.getQuantity(), testOrder.getQuantity());
        assertEquals(responseOrder.getSide().toLowerCase(), testOrder.getSide().toLowerCase());

        response = ApiCalls.getOrderById(validId);
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.body().jsonPath().get("message"), "Order not found");
    }

    // Тест на проверку ошибки при попытке удалить Order по ID, который не является Integer
    @Test(dataProvider = "nonIntegerIds")
    public void deleteOrderByNonIntegerId(String nonIntegerId) {
        Response response = ApiCalls.deleteOrderById(nonIntegerId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке удалить Order по ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds")
    public void deleteOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        Response response = ApiCalls.deleteOrderById(lessOrEqualThanZeroId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке удалить Order по ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds")
    public void deleteOrderByMoreOrEqualThanTenThousandId(String moreOrEqualThanTenThousandId) {
        Response response = ApiCalls.deleteOrderById(moreOrEqualThanTenThousandId);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке удалить Order по несуществующему ID
    @Test
    public void deleteOrderByNonExistentId() {
        // Предварительно вызываем удаление всех заказов
        ApiCalls.cleanOrderbook().then().statusCode(200);

        Response response = ApiCalls.deleteOrderById("1");
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.body().jsonPath().get("message"), "Order not found");
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
        assertEquals(response.body().jsonPath().get("message"), "Order book is clean.");

        // Проверяем, что каждый из добавленных раннее заказов удалился
        for (int i = 0; i < listOfIds.size(); i++) {
            response = ApiCalls.deleteOrderById(listOfIds.get(i));
            assertEquals(response.getStatusCode(), 404);
            assertEquals(response.body().jsonPath().get("message"), "Order not found");
        }
    }


    // Тест на проверку создания нового заказа с валидными ID
    @Test(dataProvider = "validIds")
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

    // Тест на проверку ошибки при попытке создать Order с ID, который не является Integer
    @Test(dataProvider = "nonIntegerIds")
    public void createOrderWithNonIntegerId(String nonIntegerId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(nonIntegerId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID should be an integer");
    }

    // Тест на проверку ошибки при попытке создать Order с ID, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroIds")
    public void createOrderByLessOrEqualThanZeroId(String lessOrEqualThanZeroId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(lessOrEqualThanZeroId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать Order с ID, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandIds")
    public void createOrderByMoreOrEqualThanTenThousandId(String moreOrEqualThanTenThousandId) {
        Order order = DataGenerators.createRandomOrder();
        order.setId(moreOrEqualThanTenThousandId);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "ID can't be more or equal than 10000");
    }


    // Тест на проверку создания Order с корректным значением поля price
    @Test(dataProvider = "validPrices")
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

    // Тест на проверку создания Order без поля price
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

    // Тест на проверку ошибки при попытке создать Order с price, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandPrices")
    public void createOrderByMoreOrEqualThanTenThousandPrice(Double moreOrEqualThanTenThousandPrice) {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(moreOrEqualThanTenThousandPrice);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке создать Order с price, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroPrices")
    public void createOrderByLessOrEqualThanZeroPrice(Double lessOrEqualThanZeroPrice) {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(lessOrEqualThanZeroPrice);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать Order с price, точность которой больше двух знаков после запятой
    @Test
    public void createOrderByThreeSignPrecisionPrice() {
        Order order = DataGenerators.createRandomOrder();
        order.setPrice(0.001);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Price: Incorrect number of decimal digits");
    }

    // Тест на проверку ошибки при попытке создать Order с price, который не является Double
    @Test(dataProvider = "nonDoublePrices")
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


    // Тест на проверку создания Order с корректным значением поля quantity
    @Test(dataProvider = "validQuantities")
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

    // Тест на проверку ошибки при попытке создать Order с quantity, который больше или равен 10000
    @Test(dataProvider = "moreOrEqualThanTenThousandQuantities")
    public void createOrderByMoreOrEqualThanTenThousandQuantity(Long moreOrEqualThanTenThousandQuantities) {
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(moreOrEqualThanTenThousandQuantities);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Quantity can't be more or equal than 10000");
    }

    // Тест на проверку ошибки при попытке создать Order с quantity, который меньше или равен нулю
    @Test(dataProvider = "lessOrEqualThanZeroQuantities")
    public void createOrderByLessOrEqualThanZeroQuantity(Long lessOrEqualThanZeroQuantity) {
        Order order = DataGenerators.createRandomOrder();
        order.setQuantity(lessOrEqualThanZeroQuantity);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "Quantity can't be less or equal than 0");
    }

    // Тест на проверку ошибки при попытке создать Order с quantity, который не является Long
    @Test(dataProvider = "nonLongQuantities")
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

    // Тест на проверку ошибки при попытке создать Order без quantity
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


    // Тест на проверку создания Order с корректным значением поля side
    @Test(dataProvider = "validSides")
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

    // Тест на проверку ошибки при попытке создать Order с невалидным значением поля side
    @Test(dataProvider = "nonValidSides")
    public void createOrderWithNonValidSide(String nonValidSide) {
        Order order = DataGenerators.createRandomOrder();
        order.setSide(nonValidSide);

        Response response = ApiCalls.createOrder(order);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.body().jsonPath().get("message"), "side: Incorrect value");
    }

    // Тест на проверку ошибки при попытке создать Order без side
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

    // Тест на проверку получения снэпшота marketData
    @Test
    public void getMarketDataSnapshot() {
        // Предварительно очищаем список заказов
        ApiCalls.cleanOrderbook().then().statusCode(200);

        // Добавляем 1 заказ Buy и 1 заказ Sell
        var buyOrder = DataGenerators.createRandomBuyOrder();
        ApiCalls.createOrder(buyOrder).then().statusCode(200);

        var sellOrder = DataGenerators.createRandomSellOrder();
        ApiCalls.createOrder(sellOrder).then().statusCode(200);

        // Запрашиваем снэпшот marketData
        Response response = ApiCalls.getMarketDataSnapshot();
        assertEquals(response.getStatusCode(), 200);

        // Проверяем, что в полученном снэпшоте лежат добавленные раннее заказы
        MarketDataSnapshot marketDataSnapshot = response.body().as(MarketDataSnapshot.class);
        Order marketDataSellOrder = marketDataSnapshot.getAsks().get(0);
        assertEquals(marketDataSellOrder.getPrice(), sellOrder.getPrice());
        assertEquals(marketDataSellOrder.getQuantity(), sellOrder.getQuantity());

        Order marketDataBuyOrder = marketDataSnapshot.getBids().get(0);
        assertEquals(marketDataBuyOrder.getPrice(), buyOrder.getPrice());
        assertEquals(marketDataBuyOrder.getQuantity(), buyOrder.getQuantity());
    }

    // Тест на проверку очистки marketData после вызова Clean запроса
    @Test
    public void checkIsMarketDataEmptyAfterCleanRequest() {
        // Добавляем 1 заказ Buy и 1 заказ Sell
        var buyOrder = DataGenerators.createRandomBuyOrder();
        ApiCalls.createOrder(buyOrder).then().statusCode(200);

        var sellOrder = DataGenerators.createRandomSellOrder();
        ApiCalls.createOrder(sellOrder).then().statusCode(200);

        // Запрашиваем снэпшот marketData. Проверяем, что списки заказов пустые
        Response response = ApiCalls.getMarketDataSnapshot();
        assertEquals(response.getStatusCode(), 200);
        MarketDataSnapshot marketDataSnapshot = response.body().as(MarketDataSnapshot.class);
        assertEquals(marketDataSnapshot.getAsks().size(), 1);
        assertEquals(marketDataSnapshot.getBids().size(), 1);

        // Очищаем список заказов
        ApiCalls.cleanOrderbook().then().statusCode(200);

        // Запрашиваем снэпшот marketData. Проверяем, что списки заказов пустые
        response = ApiCalls.getMarketDataSnapshot();
        assertEquals(response.getStatusCode(), 200);
        marketDataSnapshot = response.body().as(MarketDataSnapshot.class);
        assertEquals(marketDataSnapshot.getAsks().size(), 0);
        assertEquals(marketDataSnapshot.getBids().size(), 0);
    }

}
