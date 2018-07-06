package product;

import java.util.List;

/**
 * Zarządca produktów udostępniający mechanizmy pozwalające na manipulacje danych.
 *
 * @author Ryszard Poklewski-Koziełł
 */

public interface ProductManager {
    List<Product> getProductsList();

    Integer getNewPk();

    Product getProductByPK(Integer pk);

    void insertProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Product product);
}