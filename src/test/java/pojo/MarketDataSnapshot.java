package pojo;

import pojo.Order;

import java.util.List;

public class MarketDataSnapshot {

    private List<Order> asks;

    private List<Order> bids;

    public List<Order> getAsks() {
        return asks;
    }

    public void setAsks(List<Order> asks) {
        this.asks = asks;
    }

    public List<Order> getBids() {
        return bids;
    }

    public void setBids(List<Order> bids) {
        this.bids = bids;
    }
}
