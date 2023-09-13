package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.Product;

import java.util.List;
import java.util.UUID;

public interface InterfaceProductDao {

    Product insert(Product product);

    List<Product> findAll();

    Product findByName(String param);

    Product findByEan13(String param);

    UUID findHash(UUID param);

    boolean deleteById(Long id);
}
