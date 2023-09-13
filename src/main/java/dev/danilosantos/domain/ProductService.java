package dev.danilosantos.domain;

import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.infrastructure.Product;
import dev.danilosantos.infrastructure.dao.InterfaceProductDao;
import dev.danilosantos.infrastructure.dao.ProductDao;
import dev.danilosantos.infrastructure.dto.ProductInsertDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private final InterfaceProductDao dao;

    public ProductService() {
        this.dao = new ProductDao();
    }

    public void insert(ProductInsertDto dto) {
        Product product = dtoToEntity(dto);

        if (dao.findByName(product.getName()) != null &&
                product.getName().equalsIgnoreCase(dao.findByName(product.getName()).getName())) {
            throw new BaseException("nome do produto ja cadastrado");
        }

        if (dao.findByEan13(product.getEan13()) != null &&
                product.getEan13().equalsIgnoreCase(dao.findByEan13(product.getEan13()).getEan13())) {
            throw new BaseException("ean13 do produto ja cadastrado");
        }

        verifyNullValues(product);
        verifyNegativeValues(product.getPrice(), product.getQuantity(), product.getMinStorage());
        dao.insert(product);
    }

    public void deleteById(Long id) {
        if(!dao.deleteById(id)) {
            throw new BaseException("produto nao encontrado");
        }
    }

    public List<Product> findAll() {
        return dao.findAll();
    }

    private void verifyNegativeValues(Double price, Double quantity, Double minStorage) {
        if (price < 0) {
            throw new BaseException("preco nao pode ser um valor negativo");
        }
        else if (quantity < 0) {
            throw new BaseException("quantidade nao pode ser um valor negativo");
        }
        else if (minStorage < 0) {
            throw new BaseException("estoque_minimo nao pode ser um valor negativo");
        }
    }

    private void verifyNullValues(Product product) {
        if (product.getPrice() == null) {
            product.setPrice(0.0);
        }
        if (product.getQuantity() == null) {
            product.setQuantity(0.0);
        }
        if (product.getMinStorage() == null) {
            product.setMinStorage(0.0);
        }
    }

    private Product dtoToEntity(ProductInsertDto dto) {
        return new Product(
                generateUniqueHash(),
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

    private UUID generateUniqueHash() {
        UUID hash = UUID.randomUUID();

        if(dao.findHash(hash) != null) {
            while (dao.findHash(hash) != null) {
                hash = UUID.randomUUID();
            }
        }
        return hash;
    }
}
