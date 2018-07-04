package product;

public class Product {
    private String name;

    private int price;

    private Integer pk;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Integer getPk() {
        return pk;
    }

    public Product(String name, int price, Integer pk) {
        this.name = name;
        this.price = price;
        this.pk = pk;
    }
}