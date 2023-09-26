package dev.danilosantos.domain;

import dev.danilosantos.application.dto.ChangeLAtivoDto;
import dev.danilosantos.application.dto.ProductInsertDto;
import dev.danilosantos.application.dto.ProductUpdateDto;
import dev.danilosantos.domain.exception.BaseException;
import dev.danilosantos.domain.mapper.ProductMapper;
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
    public ChangeLAtivoDto changeLAtivoByHash(String hashStr) {
        try {
            Product product = dao.findByHash(UUID.fromString(hashStr));
            verifyIfProductExists(UUID.fromString(hashStr));
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
            verifyIfProductExists(UUID.fromString(hashStr));
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

    private void verifyIfProductExists(UUID hash) {
        if(dao.findByHash(hash) == null) {
            throw new BaseException("produto nao encontrado");
        }
    }

    private void verifyName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new BaseException("nome do produto nao pode ser nulo ou vazio");
        }
        if (dao.findByName(name) != null &&
                name.equalsIgnoreCase(dao.findByName(name).getNome())) {
            throw new BaseException("nome do produto ja cadastrado");
        }
    }

    private void verifyEan13(String ean13) {
        if (dao.findByEan13(ean13) != null &&
                ean13.equalsIgnoreCase(dao.findByEan13(ean13).getEan13())) {
            throw new BaseException("ean13 do produto ja cadastrado");
        }
    }

    private void verifyNumbers(Product product) {
        verifyNegativeValues(product);
        verifyNullValues(product);
    }

    private void verifyNegativeValues(Product product) {
        if (product.getPreco() < 0) {
            throw new BaseException("preco nao pode ser um valor negativo");
        }
        else if (product.getQuantidade() < 0) {
            throw new BaseException("quantidade nao pode ser um valor negativo");
        }
        else if (product.getEstoqueMin() < 0) {
            throw new BaseException("estoque_min nao pode ser um valor negativo");
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
            throw new BaseException("produto nao encontrado");
        }
        if(!baseProduct.getLAtivo()) {
            throw new BaseException("produto inativo nao pode ser atualizado");
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
