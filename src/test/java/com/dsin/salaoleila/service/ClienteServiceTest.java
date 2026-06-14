package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Cliente;
import com.dsin.salaoleila.repository.ClienteRepository;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    // ── cpfJaCadastrado ─────────────────────────────────────────────────────

    @Test
    void cpfJaCadastrado_retornaTrueSeExiste() {
        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.of(new Cliente()));

        assertThat(clienteService.cpfJaCadastrado("52998224725")).isTrue();
    }

    @Test
    void cpfJaCadastrado_retornaFalseSeNaoExiste() {
        when(clienteRepository.findByCpf("00000000000")).thenReturn(Optional.empty());

        assertThat(clienteService.cpfJaCadastrado("00000000000")).isFalse();
    }

    // ── buscarPorId ─────────────────────────────────────────────────────────

    @Test
    void buscarPorId_retornaClienteSeExiste() {
        Cliente c = new Cliente();
        c.setNome("Maria Silva");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(c));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Maria Silva");
    }

    @Test
    void buscarPorId_retornaVazioSeNaoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(clienteService.buscarPorId(99L)).isEmpty();
    }

    // ── buscarPorCpf ────────────────────────────────────────────────────────

    @Test
    void buscarPorCpf_retornaClienteSeEncontrado() {
        Cliente c = new Cliente();
        c.setCpf("52998224725");
        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.of(c));

        Optional<Cliente> resultado = clienteService.buscarPorCpf("52998224725");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCpf()).isEqualTo("52998224725");
        verify(clienteRepository).findByCpf("52998224725");
    }

    @Test
    void buscarPorCpf_retornaVazioSeCpfNaoCadastrado() {
        when(clienteRepository.findByCpf("11111111111")).thenReturn(Optional.empty());

        assertThat(clienteService.buscarPorCpf("11111111111")).isEmpty();
    }

    // ── salvar ──────────────────────────────────────────────────────────────

    @Test
    void salvar_persisteERetornaCliente() {
        Cliente c = new Cliente();
        c.setNome("Ana Souza");
        when(clienteRepository.save(c)).thenReturn(c);

        Cliente salvo = clienteService.salvar(c);

        assertThat(salvo.getNome()).isEqualTo("Ana Souza");
        verify(clienteRepository).save(c);
    }
}
