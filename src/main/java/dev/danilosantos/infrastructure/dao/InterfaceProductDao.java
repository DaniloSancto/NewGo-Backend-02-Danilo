package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.entities.Product;

import java.util.List;
import java.util.UUID;

public interface InterfaceProductDao {

    void insert(Product product);

    void updateByHash(UUID hash, Product product);

    List<Product> findAll();

    Product findByHash(UUID hash);

    boolean deleteByHash(UUID hash);

    Product findByName(String param);

    Product findByEan13(String param);

    UUID findHash(UUID param);

    void changeLAtivoToTrue(UUID hash);

    void changeLAtivoToFalse(UUID hash);

    List<Product> findAllActiveProducts();

    Product findActiveProduct(UUID param);

    List<Product> findAllInactiveProducts();
}
