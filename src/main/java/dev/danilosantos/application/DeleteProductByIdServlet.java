package dev.danilosantos.application;

import com.google.gson.Gson;
import dev.danilosantos.domain.ProductService;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.exception.JsonException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/products/*")
public class DeleteProductByIdServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    private final Gson gson = new Gson();

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            if (parts.length == 3 && "products".equals(parts[1])) {
                String productId = parts[2];
                service.deleteById(Long.parseLong(productId));
                response.setStatus(200);
            }
        } catch (BaseException e) {
            out.print(gson.toJson(new JsonException(e.getMessage())));
            response.setStatus(400);
        }
    }
}