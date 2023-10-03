package dev.danilosantos.domain.strings;

public enum ExceptionMessages {
    PRODUCT_NOT_FIND("Produto não encontrado"),
    NAME_CANNOT_BE_NULL_OR_EMPTY("Nome do produto não pode ser nulo ou vazio"),
    NAME_ALREADY_REGISTERED("Nome do produto já cadastrado"),
    EAN13_ALREADY_REGISTERED("EAN13 do produto já cadastrado"),
    PRODUCT_INACTIVE("produto inativo"),
    INACTIVE_PRODUCT_CANNOT_BE_UPDATED("Produto inativo não pode ser atualizado"),
    PRICE_CANNOT_BE_NEGATIVE("Preço não pode ser um valor negativo"),
    QUANTITY_CANNOT_BE_NEGATIVE("Quantidade não pode ser um valor negativo"),
    STORAGE_CANNOT_BE_NEGATIVE("EstoqueMin não pode ser um valor negativo"),
    INVALID_URI("URI inválida"),
    INVALID_HASH("Hash inválida"),
    PRODUCT_NOT_UPDATED("Produto não foi atualizado porque é igual ao cadastrado")
    ;

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
