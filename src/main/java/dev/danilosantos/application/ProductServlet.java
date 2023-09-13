package dev.danilosantos.application;

import com.google.gson.Gson;
import dev.danilosantos.domain.ProductService;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.exception.JsonException;
import dev.danilosantos.infrastructure.dto.ProductInsertDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    private final Gson gson = new Gson();

    // método doPost: ele pega as informações no formato JSON da requisição transforma em objeto tipo Produto e manda para a camada de serviço
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
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
            service.insert(productDto);
            response.setStatus(200);
        } catch (BaseException e) {
            out.print(gson.toJson(new JsonException(e.getMessage())));
            response.setStatus(400);
        }
    }

    // método doGet: chama o método findAll da camada de serviço retornando todos os produtos do banco de dados
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(service.findAll()));
    }

    // método doDelete: pega o terceiro parâmetro da URL (esperando ser um ID) manda para camada de serviço para deletar o produto
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
