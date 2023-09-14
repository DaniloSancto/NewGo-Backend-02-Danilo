package dev.danilosantos.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.danilosantos.domain.ProductService;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.exception.JsonError;
import dev.danilosantos.application.dto.ProductInsertDto;
import dev.danilosantos.application.dto.ProductUpdateDto;
import dev.danilosantos.infrastructure.entities.Product;

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

    // método doPost: ele pega as informações no formato JSON da requisição transforma em objeto tipo Produto e manda para a camada de serviço
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ProductInsertDto productDto = gson.fromJson(json.toString(), ProductInsertDto.class);
            service.insert(insertToEntity(productDto));
            response.setStatus(200);
        } catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
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
                service.updateByHash(hashStr, updateToEntity(productDto));
            }
            response.setStatus(200);
        } catch (BaseException e) {
            response.getWriter().write(gson.toJson(new JsonError(e.getMessage())));
            response.setStatus(400);
        }
    }

    // método doGet: chama o método findAll da camada de serviço retornando todos os produtos do banco de dados
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String requestURI = request.getRequestURI();
            String[] parts = requestURI.split("/");
            if (parts.length == 3 && "products".equals(parts[1])) {
                String hashStr = parts[2];

                response.getWriter().write(gson.toJson(service.findByHash(hashStr)));
                response.setStatus(200);
            }
            else if(parts.length == 2&& "products".equals(parts[1])) {
                response.getWriter().write(gson.toJson(service.findAll()));
            }
        } catch (BaseException e) {
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

    private Product insertToEntity(ProductInsertDto dto) {
        return new Product(dto.getName(), dto.getDescription(),dto.getEan13(),dto.getPrice(),dto.getQuantity(),dto.getMinStorage());
    }

    private Product updateToEntity(ProductUpdateDto dto) {
        return new Product(dto.getDescription(),dto.getPrice(),dto.getQuantity(),dto.getMinStorage());
    }
}
