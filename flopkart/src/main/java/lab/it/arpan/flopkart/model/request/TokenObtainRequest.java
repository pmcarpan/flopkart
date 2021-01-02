package lab.it.arpan.flopkart.model.request;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TokenObtainRequest {
    @Expose private final String username;
    @Expose private final String password;
}
