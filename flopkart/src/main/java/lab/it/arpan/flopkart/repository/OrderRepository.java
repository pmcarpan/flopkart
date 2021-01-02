package lab.it.arpan.flopkart.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import lab.it.arpan.flopkart.model.request.OrderCreateRequest;
import lab.it.arpan.flopkart.model.response.UserResponse;

public class OrderRepository {
    private final Connection connection;

    private static String CREATE_ORDER_SQL = 
        "INSERT INTO order_detail " +
            "(" +
                "email_address, mobile_number, first_name, last_name, " + 
                "shipping_address, shipping_city, shipping_state, shipping_country, shipping_zip_code, " +
                "delivery_method, " +
                "card_number, card_cvv, card_expiration, card_first_name, card_last_name, total_cost, " +
                "user_id" +
            ")" +
        "VALUES " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static String CREATE_ORDER_ITEM_SQL = 
        "INSERT INTO order_item " +
            "(order_detail_id, product_id) " +
        "VALUES " +
            "(?, ?);";

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean createOrder(OrderCreateRequest orderCreateRequest, UserResponse userResponse) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setOrderDetails(orderCreateRequest, userResponse, preparedStatement);
            
            preparedStatement.executeUpdate();
            Long orderId = preparedStatement.getGeneratedKeys().getLong(1);

            createOrderItems(orderId, orderCreateRequest.getProductIds());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createOrderItems(Long orderId, List<Long> productIds) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER_ITEM_SQL)) {
            for (Long productId : productIds) {
                preparedStatement.setLong(1, orderId);
                preparedStatement.setLong(2, productId);
                preparedStatement.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setOrderDetails(
            OrderCreateRequest orderCreateRequest, 
            UserResponse userResponse, 
            PreparedStatement preparedStatement
    ) throws SQLException {
        preparedStatement.setString(1, orderCreateRequest.getEmailAddress());
        preparedStatement.setString(2, orderCreateRequest.getMobileNumber());
        preparedStatement.setString(3, orderCreateRequest.getFirstName());
        preparedStatement.setString(4, orderCreateRequest.getLastName());
        preparedStatement.setString(5, orderCreateRequest.getAddress());
        preparedStatement.setString(6, orderCreateRequest.getCity());
        preparedStatement.setString(7, orderCreateRequest.getState());
        preparedStatement.setString(8, orderCreateRequest.getCountry());
        preparedStatement.setString(9, orderCreateRequest.getZipCode());

        preparedStatement.setString(10, orderCreateRequest.getDeliveryMethod());

        preparedStatement.setString(11, orderCreateRequest.getCardNumber());
        preparedStatement.setString(12, orderCreateRequest.getCardCvv());
        preparedStatement.setString(13, orderCreateRequest.getCardExpiration());
        preparedStatement.setString(14, orderCreateRequest.getCardFirstName());
        preparedStatement.setString(15, orderCreateRequest.getCardLastName());


        Double totalCost = 0.;
        preparedStatement.setDouble(16, totalCost);

        preparedStatement.setLong(17, userResponse.getId());
    }
}
