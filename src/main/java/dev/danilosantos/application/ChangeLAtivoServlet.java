package dev.danilosantos.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.danilosantos.domain.ProductService;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.exception.JsonError;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/products/change-lativo/*")
public class ChangeLAtivoServlet extends HttpServlet {
        private final ProductService service = new ProductService();
        private final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            if (parts.length == 4 && "change-lativo".equals(parts[2])) {
                String productHashStr = parts[3];
                response.getWriter().write(gson.toJson(service.changeLAtivoByHash(productHashStr)));
                response.setStatus(200);
            } else {
                response.getWriter().write(gson.toJson(new JsonError("passe um valor válido")));
                response.setStatus(404);
            }
        } catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }
}
