package lab.it.arpan.flopkart.controller;

import java.io.IOException;
import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import lab.it.arpan.flopkart.model.request.TokenObtainRequest;
import lab.it.arpan.flopkart.model.response.UserResponse;

@WebServlet(name = "TokenController", urlPatterns = { "/c/tokens" })
public class TokenController extends HttpServlet {
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
            case "obtain":
                doObtain(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(GSON.toJson(Map.of("action", "Invalid value.")));
                break;
        }
    }

    private void doObtain(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        TokenObtainRequest body = null;

        try {
            body = GSON.fromJson(request.getReader(), TokenObtainRequest.class);
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

        String username = body.getUsername(), password = body.getPassword();

        if (username.equals("arpan") && password.equals("password")) {
            UserResponse userResponse = UserResponse.of(1L, username, password);
            Optional<String> tokenOptional = getJwt(userResponse);
            if (tokenOptional.isPresent()) {
                response.getWriter().write(GSON.toJson(Map.of("token", tokenOptional.get())));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(GSON.toJson(Map.of("message", "Internal server error.")));
            }
            return;
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(GSON.toJson(Map.of("message", "Invalid username or password.")));
    }

    private Map<String, String> getErrors(TokenObtainRequest body) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (body == null) {
            errors.put("message", "Invalid JSON body");
            return errors;
        }

        if (body.getUsername() == null || body.getUsername().isEmpty()) 
            errors.put("username", "Value cannot be null or empty.");
        if (body.getPassword() == null || body.getPassword().isEmpty()) 
            errors.put("password", "Value cannot be null or empty.");
        return errors;
    }

    private Optional<String> getJwt(UserResponse userResponse) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("flopkartApi");
        claims.setSubject(userResponse.getUsername());
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);

        claims.setClaim("userId", userResponse.getId());

        Key key = new HmacKey("secret".getBytes());

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(key);
        jws.setDoKeyValidation(false);

        try {
            return Optional.of(jws.getCompactSerialization());
        } catch (JoseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
