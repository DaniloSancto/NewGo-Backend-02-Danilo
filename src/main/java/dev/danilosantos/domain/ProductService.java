package dev.danilosantos.domain;

import dev.danilosantos.application.dto.ChangeLAtivoDto;
import dev.danilosantos.application.dto.ProductInsertDto;
import dev.danilosantos.application.dto.ProductUpdateDto;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.infrastructure.entities.Product;
import dev.danilosantos.infrastructure.dao.InterfaceProductDao;
import dev.danilosantos.infrastructure.dao.ProductDao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private final InterfaceProductDao dao;

    public ProductService() {
        this.dao = new ProductDao();
    }

    public void insert(ProductInsertDto dto) {
        verifyIfNameAreNull(dto.getNome());

        Product product = insertDtoToEntity(dto);

        verifyIfNameOrEan13AreInUse(product.getNome(), product.getEan13());

        verifyNullValues(product);
        verifyNegativeValues(product.getPreco(), product.getQuantidade(), product.getEstoqueMin());
        dao.insert(product);
    }

    public void updateByHash(String hashStr, ProductUpdateDto dto) {
        try {
            UUID hash = UUID.fromString(hashStr);
            Product baseProduct = dao.findByHash(hash);

            updateVerifications(baseProduct);

            Product product = updateDtoToEntity(dto, baseProduct);
            verifyNullValues(product);
            verifyNegativeValues(product.getPreco(), product.getQuantidade(), product.getEstoqueMin());
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

    public ChangeLAtivoDto changeLAtivoByHash(String hashStr) {
        try {
            Product product = dao.findByHash(UUID.fromString(hashStr));

            if (product == null) {
                throw new BaseException("produto nao encontrado");
            }

            if(!product.getLAtivo()) {
                dao.changeLAtivoToTrue(UUID.fromString(hashStr));
            } else {
                dao.changeLAtivoToFalse(UUID.fromString(hashStr));
            }
            return new ChangeLAtivoDto(product.getHash(), product.getNome(), !product.getLAtivo());
        }
            catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public Product findActiveProduct(String hashStr) {
        try {
            if(dao.findByHash(UUID.fromString(hashStr)) == null) {
                throw new BaseException("produto nao encontrado");
            }
            if(dao.findActiveProduct(UUID.fromString(hashStr)) == null) {
                throw new BaseException("produto inativo");
            }
            return dao.findActiveProduct(UUID.fromString(hashStr));

        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public List<Product> findAllActiveProducts() {
        return dao.findAllActiveProducts();
    }


    public List<Product> findAllInactiveProducts() {
        return dao.findAllInactiveProducts();
    }

    public List<Product> findAllQuantityLessThanMinStorageProducts() {
        return dao.findAllQuantityLowerStorageProducts();
    }

    private void verifyIfNameAreNull(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new BaseException("nome do produto nao pode ser nulo ou vazio");
        }
    }

    private void verifyIfNameOrEan13AreInUse(String name, String ean13) {
        if (dao.findByName(name) != null &&
                name.equalsIgnoreCase(dao.findByName(name).getNome())) {
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
        if (product.getPreco() == null) {
            product.setPreco(0.0);
        }
        if (product.getQuantidade() == null) {
            product.setQuantidade(0.0);
        }
        if (product.getEstoqueMin() == null) {
            product.setEstoqueMin(0.0);
        }
    }

    private void updateVerifications(Product product) {
        if(product == null) {
            throw new BaseException("produto nao encontrado");
        }

        if(!product.getLAtivo()) {
            throw new BaseException("produto inativo nao pode ser atualizado");
        }
    }

    private Product updateDtoToEntity(ProductUpdateDto dto, Product product) {
        return new Product(
                product.getId(),
                product.getHash(),
                product.getNome(),
                verifyIfDescriptionAreNull(dto.getDescricao(), product),
                product.getEan13(),
                verifyIfPriceAreNull(dto.getPreco(), product),
                verifyIfQuantityAreNull(dto.getQuantidade(), product),
                verifyIfMinStorageAreNull(dto.getEstoqueMin(), product),
                product.getDtCreate(),
                new Date(),
                product.getLAtivo());
    }

    private String verifyIfDescriptionAreNull(String description, Product product) {
        if(description == null) return product.getDescricao();
        return description;
    }

    private Double verifyIfPriceAreNull(Double price, Product product) {
        if(price == null) return product.getPreco();
        return price;
    }

    private Double verifyIfQuantityAreNull(Double quantity, Product product) {
        if(quantity == null) return product.getQuantidade();
        return quantity;
    }

    private Double verifyIfMinStorageAreNull(Double minStorage, Product product) {
        if(minStorage == null) return product.getEstoqueMin();
        return minStorage;
    }

    private Product insertDtoToEntity(ProductInsertDto dto) {
        return new Product(
                generateUniqueHash(),
                dto.getNome(),
                dto.getDescricao(),
                dto.getEan13(),
                dto.getPreco(),
                dto.getQuantidade(),
                dto.getEstoqueMin(),
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
