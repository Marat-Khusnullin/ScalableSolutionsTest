package pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    private String id;
    //@JsonFormat(shape= JsonFormat.Shape.STRING)
    private Double price;

    //@JsonFormat(shape= JsonFormat.Shape.STRING)
    private Long quantity;

    private String side;

    public Order(String id, double price, long quantity, String side) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
    }

    public Order() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getSide() {
        return side;
    }
}
