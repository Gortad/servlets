package product;

import java.math.BigDecimal;

/**
 * Reprezentuje instancje produktu w systemie.
 *
 * @author Ryszard Poklewski-Koziełł
 */
public class Product {
    private String name;

    private BigDecimal price;

    private Integer pk;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getPk() {
        return pk;
    }

    public Product(String name, BigDecimal price, Integer pk) {
        this.name = name;
        this.price = price;
        this.pk = pk;
    }
}