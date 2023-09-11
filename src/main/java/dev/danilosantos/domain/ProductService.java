package dev.danilosantos.domain;

import dev.danilosantos.infrastructure.Product;
import dev.danilosantos.infrastructure.dao.ProductDao;
import dev.danilosantos.infrastructure.dto.ProductInsertDto;

import java.util.Date;
import java.util.UUID;

public class ProductService {
    private final ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    public void insert(ProductInsertDto dto) {
        Product product = dtoToEntity(dto);
        productDao.insert(product);
    }

    private Product dtoToEntity(ProductInsertDto dto) {
        return new Product(
                UUID.randomUUID(),
                dto.getName(),
                dto.getDescription(),
                dto.getEan13(),
                dto.getPrice(),
                dto.getQuantity(),
                dto.getMinStorage(),
                new Date(),
                null,
                false);
    }
}
