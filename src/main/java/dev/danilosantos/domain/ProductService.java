package dev.danilosantos.domain;

import dev.danilosantos.application.dto.*;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.mapper.ProductMapper;
import dev.danilosantos.domain.strings.ExceptionMessages;
import dev.danilosantos.infrastructure.entities.Product;
import dev.danilosantos.infrastructure.dao.InterfaceProductDao;
import dev.danilosantos.infrastructure.dao.ProductDao;

import java.util.*;

public class ProductService {
    private final InterfaceProductDao dao;
    private final ProductMapper mapper = new ProductMapper();

    public ProductService() {
        this.dao = new ProductDao();
    }

    public ProductDefaultResponseDto insert(ProductInsertDto dto) {
        verifyName(dto.getNome());
        verifyEan13(dto.getEan13());

        Product product = mapper.fromInsertDtoToProduct(dto, generateUniqueHash());

        verifyNumbers(product);
        return mapper.fromProductToDefaultResponseDto(dao.insert(product));
    }

    public ProductDefaultResponseDto updateByHash(String hashStr, ProductUpdateDto dto) {
        try {
            UUID hash = UUID.fromString(hashStr);

            Product baseProduct = dao.findByHash(hash);
            updateVerifications(baseProduct);

            Product product = mapper.fromUpdateDtoToProduct(dto, baseProduct);
            verifyNumbers(product);
            if(!product.toString().equals(baseProduct.toString())) {
                product.setDtUpdate(new Date());
                return mapper.fromProductToDefaultResponseDto(dao.updateByHash(hash, product));
            }
            else {
                throw new BaseException(ExceptionMessages.PRODUCT_NOT_UPDATED.getMessage());
            }
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public List<ProductDefaultResponseDto> findAll() {
        return mapper.fromListOfProductToListOfDto(dao.findAll());
    }

    public ProductDefaultResponseDto findByHash(String hashStr) {
        try {
            verifyURI(hashStr);
            verifyIfProductExists(UUID.fromString(hashStr));
            return mapper.fromProductToDefaultResponseDto(dao.findByHash(UUID.fromString(hashStr)));
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public ProductDefaultResponseDto deleteByHash(String hashStr) {
        try {
            verifyIfProductExists(UUID.fromString(hashStr));
            Product productDeleted = dao.findByHash(UUID.fromString(hashStr));
            dao.deleteByHash(productDeleted.getHash());
            return mapper.fromProductToDefaultResponseDto(productDeleted);
        }
        catch (IllegalArgumentException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public ProductDefaultResponseDto changeLAtivo(String hashStr, boolean condition) {
        verifyHash(hashStr);
        UUID hash = UUID.fromString(hashStr);
        verifyIfProductExists(hash);
        Product product = dao.findByHash(hash);
        if(condition != product.getLAtivo()) {
            dao.updateLAtivoOnDb(hash, condition);
        }
        return mapper.fromProductToDefaultResponseDto(dao.findByHash(UUID.fromString(hashStr)));
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

    public List<ProductDefaultResponseDto> findAllActiveProducts() {
        return mapper.fromListOfProductToListOfDto(dao.findAllActiveProducts());
    }


    public List<ProductDefaultResponseDto> findAllInactiveProducts() {
        return mapper.fromListOfProductToListOfDto(dao.findAllInactiveProducts());
    }

    public List<ProductDefaultResponseDto> findAllQuantityLessThanMinStorageProducts() {
        return mapper.fromListOfProductToListOfDto(dao.findAllQuantityLowerStorageProducts());
    }

    public List<Object> insertProductsInBatch (List<ProductInsertDto> listDto) {
        List<Object> response = new ArrayList<>();

        for (ProductInsertDto productInList : listDto) {
            try {
                response.add(mapper.fromProductDefaultResponseDtoToBatchResponseDto(insert(productInList), "success", "product inserted"));
            }
            catch (BaseException e) {
                response.add(new ProductInsertErrorDto(productInList.getNome(), "error", e.getMessage()));
            }
        }
        return response;
    }

    public List<Object> updateProductPriceInBatch (List<ProductUpdatePriceDto> listDto) {
        List<Object> response = new ArrayList<>();
        String field = "preco";

        for (ProductUpdatePriceDto update : listDto) {
            try {
                verifyHash(update.getHash());
                verifyIfProductExists(UUID.fromString(update.getHash()));
                UUID hash = UUID.fromString(update.getHash());
                Product baseProduct = dao.findByHash(hash);
                updateVerifications(baseProduct);

                if(update.getOperacao().equals("valor-fixo")) {
                    double newPrice = baseProduct.getPreco() + update.getValor();
                    if(newPrice < 0) {
                        throw new BaseException(ExceptionMessages.PRICE_CANNOT_BE_NEGATIVE.getMessage());
                    }
                    dao.updateProductValue(hash, newPrice, field);
                    response.add(mapper.fromProductToBatchResponseDto(dao.findByHash(hash), "success", "price updated"));
                }
                else if (update.getOperacao().equals("porcentagem")) {
                    double newPrice = baseProduct.getPreco() + (baseProduct.getPreco() * (update.getValor() / 100));
                    if(newPrice < 0) {
                        throw new BaseException(ExceptionMessages.PRICE_CANNOT_BE_NEGATIVE.getMessage());
                    }
                    dao.updateProductValue(hash, newPrice, field);
                    response.add(mapper.fromProductToBatchResponseDto(dao.findByHash(hash), "success", "price updated"));
                }
            }
            catch (BaseException baseException) {
                catchBaseExceptionOnUpdateBatch(baseException, UUID.fromString(update.getHash()), response);
            }
        }
        return response;
    }


    public List<Object> updateProductQuantityInBatch (List<ProductUpdateQuantityDto> listDto) {
        List<Object> response = new ArrayList<>();
        String field = "quantidade";

        for (ProductUpdateQuantityDto update : listDto) {
            try {
                verifyHash(update.getHash());
                verifyIfProductExists(UUID.fromString(update.getHash()));
                UUID hash = UUID.fromString(update.getHash());
                Product baseProduct = dao.findByHash(hash);
                updateVerifications(baseProduct);

                double newQuantity = baseProduct.getQuantidade() + update.getValor();
                if(newQuantity < 0) {
                    throw new BaseException(ExceptionMessages.QUANTITY_CANNOT_BE_NEGATIVE.getMessage());
                }
                dao.updateProductValue(hash, newQuantity, field);
                response.add(mapper.fromProductToBatchResponseDto(dao.findByHash(hash), "success", "quantity updated"));
            }
            catch (BaseException baseException) {
                catchBaseExceptionOnUpdateBatch(baseException, UUID.fromString(update.getHash()), response);
            }
        }
        return response;
    }

    private void catchBaseExceptionOnUpdateBatch(BaseException baseException, UUID hash, List<Object> response) {
        try {
            Product product = dao.findByHash(hash);
            if(product != null) {
                response.add(mapper.fromProductToBatchResponseDto(product, "error", baseException.getMessage()));
            }
            else {
                response.add(new ProductStandardErrorDto(hash.toString(), "error", baseException.getMessage()));
            }
        }
        catch (Exception exception) {
            response.add(new ProductStandardErrorDto(hash.toString(), "error", baseException.getMessage()));
        }
    }

    private void verifyIfProductExists(UUID hash) {
        if(dao.findByHash(hash) == null) {
            throw new BaseException(ExceptionMessages.PRODUCT_NOT_FIND.getMessage());
        }
    }

    private void verifyURI(String hashStr) {
        if(hashStr.length() != 36) {
            throw new BaseException(ExceptionMessages.INVALID_URI.getMessage());
        }
    }

    private void verifyHash(String hashStr) {
        if(hashStr.length() != 36) {
            throw new BaseException(ExceptionMessages.INVALID_HASH.getMessage());
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
