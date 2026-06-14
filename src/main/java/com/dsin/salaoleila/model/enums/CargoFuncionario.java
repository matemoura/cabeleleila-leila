package com.dsin.salaoleila.model.enums;

public enum CargoFuncionario {
    GERENTE("Gerente"),
    CABELEIREIRO("Cabeleireiro(a)"),
    MANICURE("Manicure"),
    PEDICURE("Pedicure"),
    ESTETICISTA("Esteticista"),
    AUXILIAR("Auxiliar"),
    RECEPCIONISTA("Recepcionista");

    private final String descricao;
    CargoFuncionario(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}
