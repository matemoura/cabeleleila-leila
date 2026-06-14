package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Funcionario;
import com.dsin.salaoleila.repository.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    // ── autenticar ──────────────────────────────────────────────────────────

    @Test
    void autenticar_retornaFuncionarioSeLoginESenhaCorretos() {
        Funcionario f = funcionarioComLogin("patricia", "123");
        when(funcionarioRepository.findByLoginAndSenha("patricia", "123"))
                .thenReturn(Optional.of(f));

        Optional<Funcionario> resultado = funcionarioService.autenticar("patricia", "123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getLogin()).isEqualTo("patricia");
    }

    @Test
    void autenticar_retornaVazioSeLoginIncorreto() {
        when(funcionarioRepository.findByLoginAndSenha("patricia", "errada"))
                .thenReturn(Optional.empty());

        assertThat(funcionarioService.autenticar("patricia", "errada")).isEmpty();
    }

    @Test
    void autenticar_retornaVazioSeFuncionarioNaoExiste() {
        when(funcionarioRepository.findByLoginAndSenha("naoexiste", "123"))
                .thenReturn(Optional.empty());

        assertThat(funcionarioService.autenticar("naoexiste", "123")).isEmpty();
        verify(funcionarioRepository).findByLoginAndSenha("naoexiste", "123");
    }

    // ── cpfJaCadastrado ─────────────────────────────────────────────────────

    @Test
    void cpfJaCadastrado_retornaTrueSeOutroFuncionarioTemMesmoCpf() {
        Funcionario outro = new Funcionario();
        outro.setId(99L);
        when(funcionarioRepository.findByCpf("12345678909")).thenReturn(Optional.of(outro));

        // Checking CPF for funcionário with ID 1 — outro has ID 99, so it's a conflict
        assertThat(funcionarioService.cpfJaCadastrado("12345678909", 1L)).isTrue();
    }

    @Test
    void cpfJaCadastrado_retornaFalseSeOMesmoFuncionario() {
        Funcionario f = new Funcionario();
        f.setId(1L);
        when(funcionarioRepository.findByCpf("12345678909")).thenReturn(Optional.of(f));

        // Same ID → editing own record, not a conflict
        assertThat(funcionarioService.cpfJaCadastrado("12345678909", 1L)).isFalse();
    }

    @Test
    void cpfJaCadastrado_retornaFalseSeNaoExiste() {
        when(funcionarioRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

        assertThat(funcionarioService.cpfJaCadastrado("99999999999", 1L)).isFalse();
    }

    // ── buscarPorId ─────────────────────────────────────────────────────────

    @Test
    void buscarPorId_retornaFuncionarioSeExiste() {
        Funcionario f = new Funcionario();
        f.setId(5L);
        when(funcionarioRepository.findById(5L)).thenReturn(Optional.of(f));

        assertThat(funcionarioService.buscarPorId(5L)).contains(f);
    }

    @Test
    void buscarPorId_retornaVazioSeNaoExiste() {
        when(funcionarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThat(funcionarioService.buscarPorId(999L)).isEmpty();
    }

    // ── helpers ─────────────────────────────────────────────────────────────

    private Funcionario funcionarioComLogin(String login, String senha) {
        Funcionario f = new Funcionario();
        f.setLogin(login);
        f.setSenha(senha);
        return f;
    }
}
