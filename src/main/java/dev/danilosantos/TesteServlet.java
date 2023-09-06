package dev.danilosantos;

import dev.danilosantos.infrastructure.Product;
import dev.danilosantos.infrastructure.dao.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

@WebServlet("/teste")
public class TesteServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Product entity = new Product(UUID.randomUUID(), "DESCRICAO", "NOME", "EAN13", 24.30, 25.00, 0.00, new Date(), null, false);
        PrintWriter out = response.getWriter();
        out.print(productDao.insert(entity));
    }
}