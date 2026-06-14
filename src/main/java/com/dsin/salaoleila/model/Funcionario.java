package com.dsin.salaoleila.model;

import com.dsin.salaoleila.model.enums.CargoFuncionario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Entidade que representa uma funcionária do salão (cabeleireira, manicure, gerente, etc.). */
@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @CPF(message = "CPF invalido")
    @NotBlank(message = "CPF e obrigatorio")
    @Column(unique = true)
    private String cpf;

    @NotBlank(message = "Telefone e obrigatorio")
    private String telefone;

    @Email(message = "E-mail invalido")
    @NotBlank(message = "E-mail e obrigatorio")
    private String email;

    @NotNull(message = "Cargo e obrigatorio")
    @Enumerated(EnumType.STRING)
    private CargoFuncionario cargo;

    @NotNull(message = "Data de admissao e obrigatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAdmissao;

    /** Indica se a funcionária está ativa no sistema. */
    private boolean ativo = true;

    /** Login e senha usados para acesso à área de funcionárias. */
    @Column(unique = true)
    private String login;
    private String senha;

    /** Serviços que esta funcionária está habilitada a realizar. */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "funcionario_servicos",
        joinColumns = @JoinColumn(name = "funcionario_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public CargoFuncionario getCargo() { return cargo; }
    public void setCargo(CargoFuncionario cargo) { this.cargo = cargo; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public List<Servico> getServicos() { return servicos; }
    public void setServicos(List<Servico> servicos) { this.servicos = servicos; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
