package com.dsin.salaoleila.model;

import com.dsin.salaoleila.model.enums.CategoriaItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Entidade que representa um produto no estoque do salão (ex: shampoo, tinta). */
@Entity
@Table(name = "itens_estoque")
public class ItemEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Categoria e obrigatoria")
    private CategoriaItem categoria;

    /** Unidade de medida do produto (ex: unidade, litro, kg). */
    private String unidade = "unidade";

    @NotNull(message = "Quantidade e obrigatoria")
    @Min(value = 0, message = "Quantidade nao pode ser negativa")
    private Integer quantidade = 0;

    /** Quantidade mínima aceitável — abaixo disso o painel exibe alerta de reposição. */
    @NotNull(message = "Quantidade minima e obrigatoria")
    @Min(value = 0, message = "Quantidade minima nao pode ser negativa")
    private Integer quantidadeMinima = 0;

    private BigDecimal precoUnitario;

    /** Retorna true se o estoque atual está abaixo do mínimo configurado. */
    public boolean isAbaixoDoMinimo() {
        return quantidade != null && quantidadeMinima != null && quantidade < quantidadeMinima;
    }

    /** Retorna true se não há nenhuma unidade em estoque. */
    public boolean isSemEstoque() {
        return quantidade != null && quantidade == 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public CategoriaItem getCategoria() { return categoria; }
    public void setCategoria(CategoriaItem categoria) { this.categoria = categoria; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}
