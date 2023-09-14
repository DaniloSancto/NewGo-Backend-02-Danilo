package dev.danilosantos.domain;

import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.infrastructure.entities.Product;
import dev.danilosantos.infrastructure.dao.InterfaceProductDao;
import dev.danilosantos.infrastructure.dao.ProductDao;
import dev.danilosantos.domain.dto.ProductInsertDto;
import dev.danilosantos.domain.dto.ProductUpdateDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private final InterfaceProductDao dao;

    public ProductService() {
        this.dao = new ProductDao();
    }

    public void insert(Product productToNormalize) {
        verifyIfNameAreNull(productToNormalize.getName());

        Product product = insertDtoToEntity(productToNormalize);

        verifyIfNameOrEan13AreInUse(product.getName(), product.getEan13());

        verifyNullValues(product);
        verifyNegativeValues(product.getPrice(), product.getQuantity(), product.getMinStorage());
        dao.insert(product);
    }

    public void updateByHash(String hashStr, Product productToNormalize) {
        try {
            UUID hash = UUID.fromString(hashStr);
            Product baseProduct = dao.findByHash(hash);

            updateVerifications(baseProduct);

            Product product = updateDtoToEntity(productToNormalize, baseProduct);
            verifyNullValues(product);
            verifyNegativeValues(product.getPrice(), product.getQuantity(), product.getMinStorage());
            dao.updateByHash(hash, product);
        }
                catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public List<Product> findAll() {
        return dao.findAll();
    }

    public Product findByHash(String hashStr) {
        try {
            if(dao.findHash(UUID.fromString(hashStr)) == null) {
                throw new BaseException("produto nao encontrado");
            }
            return dao.findByHash(UUID.fromString(hashStr));

        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public void deleteByHash(String hashStr) {
        try {
            if(!dao.deleteByHash(UUID.fromString(hashStr))) {
                throw new BaseException("produto nao encontrado");
            }
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public void changeToActiveByHash(String hashStr) {
        dao.changeToActiveByHash(UUID.fromString(hashStr));
    }

    private void verifyIfNameAreNull(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new BaseException("nome do produto nao pode ser nulo ou vazio");
        }
    }

    private void verifyIfNameOrEan13AreInUse(String name, String ean13) {
        if (dao.findByName(name) != null &&
                name.equalsIgnoreCase(dao.findByName(name).getName())) {
            throw new BaseException("nome do produto ja cadastrado");
        }

        if (dao.findByEan13(ean13) != null &&
                ean13.equalsIgnoreCase(dao.findByEan13(ean13).getEan13())) {
            throw new BaseException("ean13 do produto ja cadastrado");
        }
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

    private void updateVerifications(Product product) {
        if(product == null) {
            throw new BaseException("produto nao encontrado");
        }

        if(!product.getActive()) {
            throw new BaseException("produto inativo nao pode ser atualizado");
        }
    }

    private Product updateDtoToEntity(Product productToNormalize, Product product) {
        return new Product(
                product.getId(),
                product.getHash(),
                product.getName(),
                verifyIfDescriptionAreNull(productToNormalize.getDescription(), product),
                product.getEan13(),
                verifyIfPriceAreNull(productToNormalize.getPrice(), product),
                verifyIfQuantityAreNull(productToNormalize.getQuantity(), product),
                verifyIfMinStorageAreNull(productToNormalize.getMinStorage(), product),
                product.getDtCreate(),
                new Date(),
                product.getActive());
    }

    private String verifyIfDescriptionAreNull(String description, Product product) {
        if(description == null) return product.getDescription();
        return description;
    }

    private Double verifyIfPriceAreNull(Double price, Product product) {
        if(price == null) return product.getPrice();
        return price;
    }

    private Double verifyIfQuantityAreNull(Double quantity, Product product) {
        if(quantity == null) return product.getQuantity();
        return quantity;
    }

    private Double verifyIfMinStorageAreNull(Double minStorage, Product product) {
        if(minStorage == null) return product.getMinStorage();
        return minStorage;
    }

    private Product insertDtoToEntity(Product productToNormalize) {
        return new Product(
                generateUniqueHash(),
                productToNormalize.getName(),
                productToNormalize.getDescription(),
                productToNormalize.getEan13(),
                productToNormalize.getPrice(),
                productToNormalize.getQuantity(),
                productToNormalize.getMinStorage(),
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
