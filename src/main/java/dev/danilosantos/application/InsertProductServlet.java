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

@WebServlet("/product/insert")
public class InsertProductServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    private final Gson gson = new Gson();

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
}
