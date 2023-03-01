package helpers;

import pojo.Order;

import java.util.List;
import java.util.Random;

public class DataGenerators {

    public static Order createOrder(String id, double price, int quantity, String side) {
        return new Order(id, price, quantity, side);
    }

    public static Order createRandomOrder() {
        var random = new Random();
        var id = random.nextInt(10000);
        var price = random.nextInt(1000000)/100;
        var quantity = random.nextLong(10000);
        var side = List.of("Buy", "Sell").get(random.nextInt(0, 2));
        return new Order("" + id, price, quantity, side);
    }

    public static Order createRandomBuyOrder() {
        var random = new Random();
        var id = random.nextInt(10000);
        var price = random.nextInt(1000000)/100;
        var quantity = random.nextLong(10000);
        return new Order("" + id, price, quantity, "Buy");
    }

    public static Order createRandomSellOrder() {
        var random = new Random();
        var id = random.nextInt(10000);
        var price = random.nextInt(1000000)/100;
        var quantity = random.nextLong(10000);
        return new Order("" + id, price, quantity, "Sell");
    }
}
