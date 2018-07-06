package product;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacja zarządcy produktów {@link Product} zapisująca dane w pamięci.
 *
 * @author Ryszard Poklewski-Koziełł
 */
public class ProductMemoryManagerImpl implements ProductManager {
    private List<Product> products = new ArrayList<>();

    private Integer newPk = 0;

    private void updateNewPk(Product product) {
        if (newPk <= product.getPk()) {
            newPk = product.getPk() + 1;
        }
    }

    @Override
    public Integer getNewPk() {
        return newPk;
    }

    @Override
    public List<Product> getProductsList() {
        return products;
    }

    @Override
    public Product getProductByPK(Integer pk) {
        for (Product product : products) {
            if (pk.equals(product.getPk())) {
                return product;
            }
        }
        return null;
    }

    @Override
    public void insertProduct(Product product) {
        updateNewPk(product);
        products.add(product);
    }

    @Override
    public void updateProduct(Product product) {
        updateNewPk(product);
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getPk().equals(product.getPk())) {
                products.set(i, product);
                return;
            }
        }
    }

    @Override
    public void deleteProduct(Product product) {
        products.remove(product);
    }
}