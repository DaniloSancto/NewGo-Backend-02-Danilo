package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.entities.Product;

import java.util.List;
import java.util.UUID;

public interface InterfaceProductDao {

    Product insert(Product product);

    Product updateByHash(UUID hash, Product product);

    List<Product> findAll();

    Product findByHash(UUID hash);

    void deleteByHash(UUID hash);

    Product findByName(String param);

    Product findByEan13(String param);

    UUID findHash(UUID param);

    void updateLAtivoOnDb(UUID hash, boolean condition);

    List<Product> findAllActiveProducts();

    Product findActiveProduct(UUID param);

    List<Product> findAllInactiveProducts();

    List<Product> findAllQuantityLowerStorageProducts();

    void updateProductPrice(UUID hash, Double newPrice);

    void updateProductQuantity(UUID hash, Double newPrice);
}
