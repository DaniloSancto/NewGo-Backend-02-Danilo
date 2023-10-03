package dev.danilosantos.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.danilosantos.application.dto.ProductUpdatePriceDto;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        baseHeader(request, response);
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
                if (parts.length == 3) {
                    switch (parts[2]) {
                        case "insert-batch": {
                            Type produtoListType = new TypeToken<ArrayList<ProductInsertDto>>() {
                            }.getType();
                            ArrayList<ProductInsertDto> listDto = gson.fromJson(json.toString(), produtoListType);
                            response.getWriter().write(gson.toJson(service.insertProductsInBatch(listDto)));
                            response.setStatus(200);
                            break;
                        }
                        case "update-price-batch": {
                            Type produtoListType = new TypeToken<ArrayList<ProductUpdatePriceDto>>() {
                            }.getType();
                            ArrayList<ProductUpdatePriceDto> listDto = gson.fromJson(json.toString(), produtoListType);
                            response.getWriter().write(gson.toJson(service.updateProductPriceInBatch(listDto)));
                            response.setStatus(200);
                            break;
                        }
                        case "update-quantity-batch":
                            //Type produtoListType = new TypeToken<ArrayList<ProductUpdateQuantityBatchDto>>() {}.getType();
                            //ArrayList<ProductUpdateQuantityBatchDto> listDto = gson.fromJson(json.toString(), produtoListType);
                            //response.getWriter().write(gson.toJson(service.updateProductQuantityInBatch(listDto)));
                            response.setStatus(200);
                            break;
                        default:
                            response.setStatus(400);
                            break;
                    }
            }
                else if (parts.length == 2) {
                    ProductInsertDto productDto = gson.fromJson(json.toString(), ProductInsertDto.class);
                    response.getWriter().write(gson.toJson(service.insert(productDto)));
                    response.setStatus(201);
                }
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
        catch (Exception e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        baseHeader(request, response);
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
                response.getWriter().write(gson.toJson(service.updateByHash(hashStr, productDto)));
            }
            response.setStatus(200);
        } catch (Exception e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        baseHeader(request, response);
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        baseHeader(request, response);
        try {
            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            if (parts.length == 3 && "products".equals(parts[1])) {
                String productHashStr = parts[2];
                response.getWriter().write(gson.toJson(service.deleteByHash(productHashStr)));
                response.setStatus(200);
            }
        } catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }

    private void baseHeader (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    }
}
