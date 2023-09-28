package dev.danilosantos.domain;

import dev.danilosantos.application.dto.ProductInsertDto;
import dev.danilosantos.application.dto.ProductUpdateDto;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.mapper.ProductMapper;
import dev.danilosantos.domain.strings.ExceptionMessages;
import dev.danilosantos.infrastructure.entities.Product;
import dev.danilosantos.infrastructure.dao.InterfaceProductDao;
import dev.danilosantos.infrastructure.dao.ProductDao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private final InterfaceProductDao dao;
    private final ProductMapper mapper = new ProductMapper();

    public ProductService() {
        this.dao = new ProductDao();
    }

    public void insert(ProductInsertDto dto) {
        verifyName(dto.getNome());
        verifyEan13(dto.getEan13());

        Product product = mapper.fromInsertDtoToProduct(dto, generateUniqueHash());

        verifyNumbers(product);
        dao.insert(product);
    }

    public void updateByHash(String hashStr, ProductUpdateDto dto) {
        try {
            UUID hash = UUID.fromString(hashStr);

            Product baseProduct = dao.findByHash(hash);
            updateVerifications(baseProduct);

            Product product = mapper.fromUpdateDtoToProduct(dto, baseProduct);
            verifyNumbers(product);

            product.setDtUpdate(new Date());
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
            verifyIfProductExists(UUID.fromString(hashStr));
            return dao.findByHash(UUID.fromString(hashStr));
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public void deleteByHash(String hashStr) {
        try {
            verifyIfProductExists(UUID.fromString(hashStr));
            dao.deleteByHash(UUID.fromString(hashStr));
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    // alterar
    public Product changeLAtivoToTrue(String hashStr) {
        try {
            verifyIfProductExists(UUID.fromString(hashStr));
            dao.changeLAtivoToTrue(UUID.fromString(hashStr));
            return dao.findByHash(UUID.fromString(hashStr));
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public Product changeLAtivoToFalse(String hashStr) {
        try {
            verifyIfProductExists(UUID.fromString(hashStr));
            dao.changeLAtivoToFalse(UUID.fromString(hashStr));
            return dao.findByHash(UUID.fromString(hashStr));
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public Product findActiveProduct(String hashStr) {
        try {
            verifyIfProductExists(UUID.fromString(hashStr));
            if(dao.findActiveProduct(UUID.fromString(hashStr)) == null) {
                throw new BaseException(ExceptionMessages.PRODUCT_INACTIVE.getMessage());
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

    private void verifyIfProductExists(UUID hash) {
        if(dao.findByHash(hash) == null) {
            throw new BaseException(ExceptionMessages.PRODUCT_NOT_FIND.getMessage());
        }
    }

    private void verifyName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new BaseException(ExceptionMessages.NAME_CANNOT_BE_NULL_OR_EMPTY.getMessage());
        }
        if (dao.findByName(name) != null &&
                name.equalsIgnoreCase(dao.findByName(name).getNome())) {
            throw new BaseException(ExceptionMessages.NAME_ALREADY_REGISTERED.getMessage());
        }
    }

    private void verifyEan13(String ean13) {
        if (dao.findByEan13(ean13) != null &&
                ean13.equalsIgnoreCase(dao.findByEan13(ean13).getEan13())) {
            throw new BaseException(ExceptionMessages.EAN13_ALREADY_REGISTERED.getMessage());
        }
    }

    private void verifyNumbers(Product product) {
        verifyNullValues(product);
        verifyNegativeValues(product);
    }

    private void verifyNegativeValues(Product product) {
        if (product.getPreco() < 0) {
            throw new BaseException(ExceptionMessages.PRICE_CANNOT_BE_NEGATIVE.getMessage());
        }
        else if (product.getQuantidade() < 0) {
            throw new BaseException(ExceptionMessages.QUANTITY_CANNOT_BE_NEGATIVE.getMessage());
        }
        else if (product.getEstoqueMin() < 0) {
            throw new BaseException(ExceptionMessages.STORAGE_CANNOT_BE_NEGATIVE.getMessage());
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

    private void updateVerifications(Product baseProduct) {
        if(baseProduct == null) {
            throw new BaseException(ExceptionMessages.PRODUCT_NOT_FIND.getMessage());
        }
        if(!baseProduct.getLAtivo()) {
            throw new BaseException(ExceptionMessages.INACTIVE_PRODUCT_CANNOT_BE_UPDATED.getMessage());
        }
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
