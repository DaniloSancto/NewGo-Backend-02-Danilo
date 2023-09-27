package dev.danilosantos.application.dto;

import java.util.UUID;

public class ChangeLAtivoDto {
    private UUID hash;
    private String nome;
    private Boolean lAtivo;

    public ChangeLAtivoDto(UUID hash, String nome, Boolean lAtivo) {
        this.hash = hash;
        this.nome = nome;
        this.lAtivo = lAtivo;
    }

    public UUID getHash() {
        return hash;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getlAtivo() {
        return lAtivo;
    }
}
