package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.model.Cliente;
import com.dsin.salaoleila.model.Servico;
import com.dsin.salaoleila.model.enums.StatusAgendamento;
import com.dsin.salaoleila.repository.AgendamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private AgendamentoService agendamentoService;

    // ── podeEditar ──────────────────────────────────────────────────────────

    @Test
    void podeEditar_retornaTrueSeAgendamentoEhMaisDeDoiDias() {
        Agendamento ag = new Agendamento();
        ag.setDataHora(LocalDateTime.now().plusDays(5));

        assertThat(agendamentoService.podeEditar(ag)).isTrue();
    }

    @Test
    void podeEditar_retornaFalseSeAgendamentoEhAmanha() {
        Agendamento ag = new Agendamento();
        ag.setDataHora(LocalDateTime.now().plusDays(1));

        assertThat(agendamentoService.podeEditar(ag)).isFalse();
    }

    @Test
    void podeEditar_retornaFalseSeDataJaPassou() {
        Agendamento ag = new Agendamento();
        ag.setDataHora(LocalDateTime.now().minusDays(3));

        assertThat(agendamentoService.podeEditar(ag)).isFalse();
    }

    // ── listarPorCliente ────────────────────────────────────────────────────

    @Test
    void listarPorCliente_delegaParaRepositorioERetornaResultado() {
        Cliente cliente = new Cliente();
        Agendamento ag = new Agendamento();
        when(agendamentoRepository.findByClienteOrderByDataHoraDesc(cliente))
                .thenReturn(List.of(ag));

        List<Agendamento> resultado = agendamentoService.listarPorCliente(cliente);

        assertThat(resultado).containsExactly(ag);
        verify(agendamentoRepository).findByClienteOrderByDataHoraDesc(cliente);
    }

    @Test
    void listarPorCliente_retornaListaVaziaQuandoClienteSemAgendamentos() {
        Cliente cliente = new Cliente();
        when(agendamentoRepository.findByClienteOrderByDataHoraDesc(cliente))
                .thenReturn(List.of());

        assertThat(agendamentoService.listarPorCliente(cliente)).isEmpty();
    }

    // ── buscarPorId ─────────────────────────────────────────────────────────

    @Test
    void buscarPorId_retornaAgendamentoSeExiste() {
        Agendamento ag = new Agendamento();
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(ag));

        assertThat(agendamentoService.buscarPorId(1L)).contains(ag);
    }

    @Test
    void buscarPorId_retornaVazioSeNaoExiste() {
        when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(agendamentoService.buscarPorId(99L)).isEmpty();
    }

    // ── buscarHorariosOcupados ──────────────────────────────────────────────

    @Test
    void buscarHorariosOcupados_ignoraAgendamentosCancelados() {
        LocalDate data = LocalDate.of(2026, 6, 17);
        Servico servico = servico("Corte", 60);

        Agendamento cancelado = new Agendamento();
        cancelado.setDataHora(data.atTime(10, 0));
        cancelado.setStatus(StatusAgendamento.CANCELADO);
        cancelado.setServicos(List.of(servico));

        when(agendamentoRepository.findByDataHoraBetweenOrderByDataHora(any(), any()))
                .thenReturn(List.of(cancelado));

        assertThat(agendamentoService.buscarHorariosOcupados(data)).isEmpty();
    }

    @Test
    void buscarHorariosOcupados_retornaSlotsDe30MinParaServicoDe60Min() {
        LocalDate data = LocalDate.of(2026, 6, 17);
        Servico servico = servico("Corte Feminino", 60);

        Agendamento ag = new Agendamento();
        ag.setDataHora(data.atTime(10, 0));
        ag.setStatus(StatusAgendamento.CONFIRMADO);
        ag.setServicos(List.of(servico));

        when(agendamentoRepository.findByDataHoraBetweenOrderByDataHora(any(), any()))
                .thenReturn(List.of(ag));

        List<String> horarios = agendamentoService.buscarHorariosOcupados(data);

        assertThat(horarios).containsExactlyInAnyOrder("10:00", "10:30");
    }

    @Test
    void buscarHorariosOcupados_usaDuracaoDefaultDe30MinQuandoServicosSemDuracao() {
        LocalDate data = LocalDate.of(2026, 6, 17);
        Servico servico = servico("Serviço Sem Duração", 0);

        Agendamento ag = new Agendamento();
        ag.setDataHora(data.atTime(11, 0));
        ag.setStatus(StatusAgendamento.CONFIRMADO);
        ag.setServicos(List.of(servico));

        when(agendamentoRepository.findByDataHoraBetweenOrderByDataHora(any(), any()))
                .thenReturn(List.of(ag));

        List<String> horarios = agendamentoService.buscarHorariosOcupados(data);

        assertThat(horarios).containsExactly("11:00");
    }

    // ── helpers ─────────────────────────────────────────────────────────────

    private Servico servico(String nome, int duracaoMinutos) {
        Servico s = new Servico();
        s.setNome(nome);
        s.setDuracaoMinutos(duracaoMinutos);
        s.setPreco(new BigDecimal("50.00"));
        return s;
    }
}
