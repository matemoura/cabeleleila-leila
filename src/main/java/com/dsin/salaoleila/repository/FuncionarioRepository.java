package com.dsin.salaoleila.repository;

import com.dsin.salaoleila.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Repositório de funcionárias — consultas derivadas do Spring Data JPA. */
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByCpf(String cpf);
    Optional<Funcionario> findByLoginAndSenha(String login, String senha);
    List<Funcionario> findByAtivoTrueOrderByNome();
    List<Funcionario> findAllByOrderByNome();
}
