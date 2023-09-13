package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.Product;

import java.util.List;
import java.util.UUID;

public interface InterfaceProductDao {

    Product insert(Product product);

    boolean updateByHash(UUID hash, Product product);

    List<Product> findAll();

    Product findByName(String param);

    Product findByEan13(String param);

    Product findByHash(UUID hash);

    UUID findHash(UUID param);

    boolean deleteByHash(UUID hash);
}
