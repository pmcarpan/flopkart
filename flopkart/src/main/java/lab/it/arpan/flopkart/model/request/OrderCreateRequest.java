package lab.it.arpan.flopkart.model.request;

import java.util.List;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class OrderCreateRequest {
    // Products in cart.
    @Expose private final List<Long> productIds;
    
    // Basic shipping info.
    @Expose final String emailAddress;
    @Expose final String mobileNumber;
    @Expose final String firstName;
    @Expose final String lastName;
    @Expose final String address;
    @Expose final String city;
    @Expose final String state;
    @Expose final String country;
    @Expose final String zipCode;

    // Delivery method.
    @Expose private final String deliveryMethod;
    
    // Payment details.
    @Expose final String cardNumber;
    @Expose final String cardCvv;
    @Expose final String cardExpiration;
    @Expose final String cardFirstName;
    @Expose final String cardLastName;
    @Expose final Double totalCost;
}
