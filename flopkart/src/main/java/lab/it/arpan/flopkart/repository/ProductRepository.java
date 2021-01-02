package lab.it.arpan.flopkart.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lab.it.arpan.flopkart.model.response.ProductResponse;
import lab.it.arpan.flopkart.utils.ResultSetUtilities;

public class ProductRepository {
    private final Connection connection;

    private static String LIST_PRODUCTS_SQL = 
        "SELECT "
            + "product.id, product_name, product_description, category_name, new_arrival.id as new_arrival_id, "
            + "price, discount.id as discount_id, discount_percentage, image_file_url, footnote "
        + "FROM product "
        + "INNER JOIN category "
        + "ON product.category_id = category.id "
        + "LEFT JOIN discount "
        + "ON product.id = discount.product_id "
        + "LEFT JOIN new_arrival "
        + "ON product.id = new_arrival.product_id;";

    private static String RETRIEVE_PRODUCT_SQL = 
        "SELECT "
            + "product.id, product_name, product_description, category_name, new_arrival.id as new_arrival_id, "
            + "price, discount.id as discount_id, discount_percentage, image_file_url, footnote "
        + "FROM product "
        + "INNER JOIN category "
        + "ON product.category_id = category.id "
        + "LEFT JOIN discount "
        + "ON product.id = discount.product_id "
        + "LEFT JOIN new_arrival "
        + "ON product.id = new_arrival.product_id "
        + "WHERE product.id = ?;";

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public List<ProductResponse> retrieveProducts() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(LIST_PRODUCTS_SQL);
            List<ProductResponse> products = new ArrayList<>();
            while (rs.next()) {
                products.add(constructProductResponse(rs));
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<ProductResponse> retrieveProduct(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RETRIEVE_PRODUCT_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return Optional.of(constructProductResponse(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    private ProductResponse constructProductResponse(ResultSet rs) throws SQLException {
        Double price = rs.getDouble("price");
        Double discountPercentage = ResultSetUtilities.getDoubleObject(rs, "discount_percentage");
        Double reducedPrice = null;
        if (discountPercentage != null) {
            reducedPrice = price * (100 - discountPercentage) / 100;
            reducedPrice = Math.round(reducedPrice * 100) / 100.0;
        }

        Boolean isNewArrival = ResultSetUtilities.getLongObject(rs, "new_arrival_id") != null;

        return ProductResponse.of(
            rs.getLong("id"), 
            rs.getString("product_name"), 
            rs.getString("product_description"), 
            rs.getString("category_name"), 
            isNewArrival, 
            price,
            discountPercentage, 
            reducedPrice,
            rs.getString("image_file_url"),
            rs.getString("footnote")
        );
    }
}
