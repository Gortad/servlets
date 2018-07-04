package product;

import java.util.ArrayList;
import java.util.List;


public class ProductMemoryManagerImpl implements ProductManager {
    private List<Product> products = new ArrayList<>();

    private Integer newPk = 0;

    private void updateNewPk(Product product) {
        if (newPk <= product.getPk()) {
            newPk = product.getPk() + 1;
        }
    }

    public Integer getNewPk() {
        return newPk;
    }

    public List<Product> getProductsList() {
        return products;
    }

    public Product getProductByPK(Integer pk) {
        for (Product product : products) {
            if (pk.equals(product.getPk())) {
                return product;
            }
        }
        return null;
    }

    public void insertProduct(Product product) {
        updateNewPk(product);
        products.add(product);
    }

    public void updateProduct(Product product) {
        updateNewPk(product);
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getPk().equals(product.getPk())) {
                products.set(i, product);
                return;
            }
        }
    }

    public void deleteProduct(Product product) {
        products.remove(product);
    }
}