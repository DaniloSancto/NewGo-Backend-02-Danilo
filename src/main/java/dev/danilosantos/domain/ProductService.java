package dev.danilosantos.domain;

import dev.danilosantos.domain.exception.BaseException;
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

        if (productDao.findByName(product.getName()) != null &&
                product.getName().equalsIgnoreCase(productDao.findByName(product.getName()).getName())) {
            throw new BaseException("nome do produto ja cadastrado");
        }

        if (productDao.findByEan13(product.getEan13()) != null &&
                product.getEan13().equalsIgnoreCase(productDao.findByEan13(product.getEan13()).getEan13())) {
            throw new BaseException("ean13 do produto ja cadastrado");
        }

        verifyNegativeValues(product.getPrice(), product.getQuantity(), product.getMinStorage());
        productDao.insert(product);
    }

    private void verifyNegativeValues(Double price, Double quantity, Double minStorage) {
        if (price< 0) {
            throw new BaseException("preco nao pode ser um valor negativo");
        }
        else if (quantity < 0) {
            throw new BaseException("quantidade nao pode ser um valor negativo");
        }
        else if (minStorage < 0) {
            throw new BaseException("estoque_minimo nao pode ser um valor negativo");
        }
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
