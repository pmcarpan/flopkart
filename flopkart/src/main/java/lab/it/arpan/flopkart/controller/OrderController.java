package lab.it.arpan.flopkart.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import lab.it.arpan.flopkart.model.request.OrderCreateRequest;
import lab.it.arpan.flopkart.model.response.UserResponse;
import lab.it.arpan.flopkart.repository.OrderRepository;

@WebServlet(name = "OrderController", urlPatterns = { "/c/orders" })
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Gson GSON = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(GSON.toJson(Map.of("action", "Cannot be empty.")));
            return;
        }

        switch (action) {
            case "create":
                doCreate(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(GSON.toJson(Map.of("action", "Invalid value.")));
                break;
        }
    }

    private void doCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        OrderCreateRequest body = null;

        try {
            body = GSON.fromJson(request.getReader(), OrderCreateRequest.class);
        } catch (JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(GSON.toJson(Map.of("message", "Invalid JSON body.")));
            return;
        }

        Map<String, String> jsonErrors = getErrors(body);
        if (!jsonErrors.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(GSON.toJson(jsonErrors));
            return;
        }

        OrderRepository orderRepository = (OrderRepository) getServletContext().getAttribute("orderRepository");

        UserResponse user = UserResponse.of(1L, "arpan", "password");

        if (orderRepository.createOrder(body, user)) {
            return;
        }

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(GSON.toJson(Map.of("message", "Internal server error.")));
    }

    private Map<String, String> getErrors(OrderCreateRequest body) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (body == null) {
            errors.put("message", "Invalid JSON body");
            return errors;
        }

        if (body.getProductIds() == null || body.getProductIds().isEmpty()) 
            errors.put("productIds", "Value cannot be null or empty.");
        
        if (body.getEmailAddress() == null || body.getEmailAddress().isEmpty()) 
            errors.put("emailAddress", "Value cannot be null or empty.");

        if (body.getMobileNumber() == null || body.getMobileNumber().isEmpty()) 
            errors.put("mobileNumber", "Value cannot be null or empty.");
        
        if (body.getFirstName() == null || body.getFirstName().isEmpty()) 
            errors.put("firstName", "Value cannot be null or empty.");

        if (body.getLastName() == null || body.getLastName().isEmpty()) 
            errors.put("lastName", "Value cannot be null or empty.");

        if (body.getAddress() == null || body.getAddress().isEmpty()) 
            errors.put("address", "Value cannot be null or empty.");
        
        if (body.getCity() == null || body.getCity().isEmpty()) 
            errors.put("city", "Value cannot be null or empty.");

        if (body.getState() == null || body.getState().isEmpty()) 
            errors.put("state", "Value cannot be null or empty.");

        if (body.getCountry() == null || body.getCountry().isEmpty()) 
            errors.put("country", "Value cannot be null or empty.");
        
        if (body.getZipCode() == null || body.getZipCode().isEmpty()) 
            errors.put("zipCode", "Value cannot be null or empty.");

        if (body.getDeliveryMethod() == null || body.getDeliveryMethod().isEmpty()) 
            errors.put("deliveryMethod", "Value cannot be null or empty.");

        if (body.getCardNumber() == null || body.getCardNumber().isEmpty()) 
            errors.put("cardNumber", "Value cannot be null or empty.");
        
        if (body.getCardCvv() == null || body.getCardCvv().isEmpty()) 
            errors.put("cardCvv", "Value cannot be null or empty.");

        if (body.getCardExpiration() == null || body.getCardExpiration().isEmpty()) 
            errors.put("cardExpiration", "Value cannot be null or empty.");

        if (body.getCardFirstName() == null || body.getCardFirstName().isEmpty()) 
            errors.put("cardFirstName", "Value cannot be null or empty.");
        
        if (body.getCardLastName() == null || body.getCardLastName().isEmpty()) 
            errors.put("cardLastName", "Value cannot be null or empty.");

        return errors;
    }
}
