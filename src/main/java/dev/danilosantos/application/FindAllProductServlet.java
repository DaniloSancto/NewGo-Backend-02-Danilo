package dev.danilosantos.application;

import com.google.gson.Gson;
import dev.danilosantos.domain.ProductService;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.exception.JsonException;
import dev.danilosantos.infrastructure.Product;
import dev.danilosantos.infrastructure.dto.ProductInsertDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/products")
public class FindAllProductServlet extends HttpServlet {
    private final ProductService service = new ProductService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(service.findAll()));
    }
}
