package product;

import java.util.List;

public interface ProductManager {
    List<Product> getProductsList();

    Integer getNewPk();

    Product getProductByPK(Integer pk);

    void insertProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Product product);
}