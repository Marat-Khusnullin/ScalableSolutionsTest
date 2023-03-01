import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class GetMarketDataSnapshotTests {

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
