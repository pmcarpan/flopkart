package lab.it.arpan.flopkart.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "StaticFileController", urlPatterns = { "/static/*" })
public class StaticFileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String staticFilesRoot;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.staticFilesRoot = config.getServletContext().getInitParameter("staticRoot");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");

        File file = new File(staticFilesRoot, filename);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
