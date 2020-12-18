package lab.it.arpan.flopkart.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lab.it.arpan.flopkart.repository.ProductRepository;

@WebListener(value = "RepositoryCreatorListener")
public class RepositoryCreatorListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/com/Desktop/flopkart/flopkart/flopkart.db");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ServletContext context = event.getServletContext();

        ProductRepository productRepository = new ProductRepository(connection);

        context.setAttribute("connection", connection);
        context.setAttribute("productRepository", productRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Connection connection = (Connection) event.getServletContext().getAttribute("connection");

        try {
            connection.close();
            DriverManager.deregisterDriver(DriverManager.getDriver("jdbc:sqlite:C:/Users/com/Desktop/flopkart/flopkart/flopkart.db"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
