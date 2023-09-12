package dev.danilosantos.infrastructure.dao;

import dev.danilosantos.infrastructure.Product;

import java.util.UUID;

public interface InterfaceProductDao {

    Product insert(Product product);

    Product findByName(String param);

    Product findByEan13(String param);

    UUID findHash(UUID param);
}
