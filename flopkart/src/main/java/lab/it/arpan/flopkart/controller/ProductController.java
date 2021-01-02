package lab.it.arpan.flopkart.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import lab.it.arpan.flopkart.model.response.ProductResponse;
import lab.it.arpan.flopkart.repository.ProductRepository;
import lab.it.arpan.flopkart.utils.NumberParseUtilities;

@WebServlet(name = "ProductController", urlPatterns = { "/c/products" })
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(GSON.toJson(Map.of("action", "Cannot be empty.")));
            return;
        }

        switch (action) {
            case "list":
                doList(request, response);
                break;
            case "retrieve":
                doRetrieve(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(GSON.toJson(Map.of("action", "Invalid value.")));
                break;
        }
    }

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
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(GSON.toJson(Map.of("action", "Invalid value.")));
                break;
        }
    }

    private void doList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ProductRepository productRepository = (ProductRepository) getServletContext().getAttribute("productRepository");
        response.getWriter().write(GSON.toJson(productRepository.retrieveProducts()));
    }

    private void doRetrieve(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ProductRepository productRepository = (ProductRepository) getServletContext().getAttribute("productRepository");
        
        String idQueryParameter = request.getParameter("id");
        Optional<Long> idOptional = NumberParseUtilities.parseLong(idQueryParameter);

        if (!idOptional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(GSON.toJson(Map.of("id", "Invalid value.")));
            return;
        }

        Optional<ProductResponse> productOptional = productRepository.retrieveProduct(idOptional.get());

        if (!productOptional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(GSON.toJson(Map.of("message", "Not found.")));
            return;
        }

        response.getWriter().write(GSON.toJson(productOptional.get()));
    }
}
