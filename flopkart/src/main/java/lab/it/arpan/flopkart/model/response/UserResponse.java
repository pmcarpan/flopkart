package lab.it.arpan.flopkart.model.response;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserResponse {
    private final Long id;
    private final String username;
    private final String password;
}
