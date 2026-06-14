package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Funcionario;
import com.dsin.salaoleila.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAllByOrderByNome();
    }

    public List<Funcionario> listarAtivos() {
        return funcionarioRepository.findByAtivoTrueOrderByNome();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    // idAtual serve pra ignorar o próprio registro na hora de editar
    public boolean cpfJaCadastrado(String cpf, Long idAtual) {
        return funcionarioRepository.findByCpf(cpf)
            .filter(f -> !f.getId().equals(idAtual))
            .isPresent();
    }

    public Funcionario salvar(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public Optional<Funcionario> autenticar(String login, String senha) {
        return funcionarioRepository.findByLoginAndSenha(login, senha);
    }

    public void toggleAtivo(Long id) {
        funcionarioRepository.findById(id).ifPresent(f -> {
            f.setAtivo(!f.isAtivo());
            funcionarioRepository.save(f);
        });
    }
}
