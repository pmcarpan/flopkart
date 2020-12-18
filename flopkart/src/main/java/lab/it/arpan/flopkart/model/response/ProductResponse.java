package lab.it.arpan.flopkart.model.response;

import lombok.Value;

@Value(staticConstructor = "of")
public class ProductResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String category;
    private final Boolean isNewArrival;
    private final Double price;
    private final Double discountPercentage;
    private final Double reducedPrice;
    private final String imageUrl;
    private final String footnote;
}
