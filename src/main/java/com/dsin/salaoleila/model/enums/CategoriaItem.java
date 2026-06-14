package com.dsin.salaoleila.model.enums;

public enum CategoriaItem {
    TINTURA("Tintura e Coloracao"),
    SHAMPOO("Shampoo e Condicionador"),
    TRATAMENTO("Tratamento Capilar"),
    MANICURE("Material de Manicure"),
    DESCARTAVEL("Descartaveis"),
    LIMPEZA("Limpeza e Higiene"),
    OUTRO("Outros");

    private final String descricao;

    CategoriaItem(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
