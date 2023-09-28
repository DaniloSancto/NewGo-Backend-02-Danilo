package dev.danilosantos.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.danilosantos.domain.ProductService;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.exception.JsonError;
import dev.danilosantos.application.dto.ProductInsertDto;
import dev.danilosantos.application.dto.ProductUpdateDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            BufferedReader reader = request.getReader();

            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");

            if (reader.ready()) {
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line.trim());
                }
                ProductInsertDto productDto = gson.fromJson(json.toString(), ProductInsertDto.class);
                service.insert(productDto);
                response.setStatus(200);
            }
            else if (parts.length == 4) {
                if(parts[3].equals("activate")) {
                    String productHashStr = parts[2];
                    response.getWriter().write(gson.toJson(service.changeLAtivoToTrue(productHashStr)));
                    response.setStatus(200);
                }
                else if (parts[3].equals("deactivate")) {
                    String productHashStr = parts[2];
                    response.getWriter().write(gson.toJson(service.changeLAtivoToFalse(productHashStr)));
                    response.setStatus(200);
                }
                else {
                    response.setStatus(505);
                }
            }
            else {
                response.setStatus(505);
            }
        }
        catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
        catch (Exception e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(406);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line.trim());
            }
            ProductUpdateDto productDto = gson.fromJson(json.toString(), ProductUpdateDto.class);

            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            if (parts.length == 3 && "products".equals(parts[1])) {
                String hashStr = parts[2];
                service.updateByHash(hashStr, productDto);
            }
            response.setStatus(200);
        } catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        } catch (Exception e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(406);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            String param = request.getParameter("lativo");

            if (parts.length == 4 && "active".equals(parts[3])) {
                String hashStr = parts[2];
                response.getWriter().write(gson.toJson(service.findActiveProduct(hashStr)));
                response.setStatus(200);
            }
            else if (parts.length == 2 && param != null) {
                boolean lAtivo = Boolean.parseBoolean(param);
                if (lAtivo) {
                    response.getWriter().write(gson.toJson(service.findAllActiveProducts()));
                    response.setStatus(200);
                } else {
                    response.getWriter().write(gson.toJson(service.findAllInactiveProducts()));
                    response.setStatus(200);
                }
            }
            else if (request.getParameterMap().isEmpty()) {
                if (parts.length == 3 && (parts[2].equals("quantity-less-than-min-storage"))) {
                    response.getWriter().write(gson.toJson(service.findAllQuantityLessThanMinStorageProducts()));
                    response.setStatus(200);
                }
                else if (parts.length == 3) {
                    String hashStr = parts[2];
                    response.getWriter().write(gson.toJson(service.findByHash(hashStr)));
                    response.setStatus(200);
                }
                else if (parts.length == 2) {
                    response.getWriter().write(gson.toJson(service.findAll()));
                    response.setStatus(200);
                }
            }
            else {
                response.setStatus(505);
            }
        }
        catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }

    // método doDelete: pega o terceiro parâmetro da URL (esperando ser um UUID) manda para camada de serviço para deletar o produto
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            if (parts.length == 3 && "products".equals(parts[1])) {
                String productHashStr = parts[2];
                service.deleteByHash(productHashStr);
                response.setStatus(200);
            }
        } catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }
}
