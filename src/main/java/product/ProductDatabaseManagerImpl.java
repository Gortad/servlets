package product;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Implementacja zarządcy produktów {@link Product} korzystająca z bazy danych.
 *
 * @author Ryszard Poklewski-Koziełł
 */
public class ProductDatabaseManagerImpl implements ProductManager {

    private Connection getDirectConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "admin", "admin");
    }

    private Connection getDataSourceConnection() throws SQLException {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:/PostgresDS");
            return dataSource.getConnection();
        } catch (NamingException e) {
            System.err.println(e);
            return null;
        }
    }

    @Override
    public Integer getNewPk() {
        try (Connection connection = getDataSourceConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT MAX(pk) FROM product");
            if(resultSet == null) {
                return 1;
            }
            resultSet.next();
            Integer maxPk = resultSet.getInt(1);
            return maxPk + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public List<Product> getProductsList() {
        try (Connection connection = getDataSourceConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT pk, name, price FROM product");
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("pk"));
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection error during getList");
            return new ArrayList<>();
        }
    }

    @Override
    public Product getProductByPK(Integer pk) {
        try (Connection connection = getDataSourceConnection()) {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT pk, name, price FROM product Where pk=?");
            preparedStatement.setInt(1, pk);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                return new Product(resultSet.getString("name"), resultSet.getBigDecimal("price"), resultSet.getInt("pk"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Connection error during getProduct");
            return null;
        }
    }

    @Override
    public void insertProduct(Product product) {
        try (Connection connection = getDataSourceConnection()) {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("INSERT INTO product(name, price) VALUES (?, ?)");
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Connection error during insert");
        }
    }

    @Override
    public void updateProduct(Product product) {
        try (Connection connection = getDataSourceConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product " +
                    " SET name = ?, price = ? WHERE pk = ?");
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());
            preparedStatement.setInt(3, product.getPk());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Connection error during update");
        }
    }

    @Override
    public void deleteProduct(Product product) {
        try (Connection connection = getDataSourceConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM product " +
                    " WHERE pk = ?;");
            preparedStatement.setInt(1, product.getPk());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Connection error during delete");
        }
    }
}
