package com.dsin.salaoleila.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/** Entidade que representa um serviço oferecido pelo salão (ex: Corte, Coloração). */
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    private String descricao;

    @NotNull(message = "Preco e obrigatorio")
    private BigDecimal preco;

    /** Duração estimada do serviço em minutos — usada para bloquear horários na agenda. */
    @NotNull(message = "Duracao e obrigatoria")
    private Integer duracaoMinutos;

    /** Nome do arquivo de imagem em static/images/ exibido na página inicial. */
    private String imagem;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}
