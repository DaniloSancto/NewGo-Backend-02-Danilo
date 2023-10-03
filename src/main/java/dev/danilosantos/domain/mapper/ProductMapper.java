package dev.danilosantos.domain.mapper;

import dev.danilosantos.application.dto.ProductInsertDto;
import dev.danilosantos.application.dto.ProductDefaultResponseDto;
import dev.danilosantos.application.dto.ProductUpdateDto;
import dev.danilosantos.infrastructure.entities.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProductMapper {

    public Product fromInsertDtoToProduct(ProductInsertDto dto, UUID hash) {
        return new Product(
                hash,
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

    public Product fromUpdateDtoToProduct(ProductUpdateDto dto, Product product) {
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
                product.getDtUpdate(),
                product.getLAtivo());
    }

    public ProductDefaultResponseDto fromProductToDefaultResponseDto(Product product) {
        return new ProductDefaultResponseDto(
                product.getHash(),
                product.getNome(),
                product.getDescricao(),
                product.getEan13(),
                product.getPreco(),
                product.getQuantidade(),
                product.getEstoqueMin(),
                product.getDtCreate(),
                product.getDtUpdate(),
                product.getLAtivo());
    }

    public List<ProductDefaultResponseDto> fromListOfProductToListOfDto(List<Product> productList) {
        List<ProductDefaultResponseDto> dtoList = new ArrayList<>();
        for (Product product: productList) {
            dtoList.add(fromProductToDefaultResponseDto(product));
        }
        return dtoList;
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
}
