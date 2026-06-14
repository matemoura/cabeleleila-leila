package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.model.Cliente;
import com.dsin.salaoleila.model.enums.StatusAgendamento;
import com.dsin.salaoleila.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public Agendamento salvar(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    // edição só é permitida se faltam mais de 2 dias
    public boolean podeEditar(Agendamento agendamento) {
        LocalDateTime limiteEdicao = agendamento.getDataHora().minusDays(2);
        return LocalDateTime.now().isBefore(limiteEdicao);
    }

    public Agendamento atualizar(Agendamento agendamento) {
        if (!podeEditar(agendamento)) {
            throw new IllegalStateException(
                "Não é possível editar. Faltam menos de 2 dias. Entre em contato por telefone."
            );
        }
        return agendamentoRepository.save(agendamento);
    }

    // se o cliente já tem agendamento na mesma semana, sugere o mesmo horário
    public Optional<LocalDateTime> sugerirData(Cliente cliente, LocalDateTime dataDesejada) {
        LocalDateTime inicioDaSemana = dataDesejada
            .with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime fimDaSemana = dataDesejada
            .with(DayOfWeek.SUNDAY).toLocalDate().atTime(23, 59, 59);

        List<Agendamento> agendamentosNaSemana = agendamentoRepository
            .findByClienteAndDataHoraBetween(cliente, inicioDaSemana, fimDaSemana);

        if (!agendamentosNaSemana.isEmpty()) {
            return Optional.of(agendamentosNaSemana.get(0).getDataHora());
        }

        return Optional.empty();
    }

    public List<Agendamento> listarPorCliente(Cliente cliente) {
        return agendamentoRepository.findByClienteOrderByDataHoraDesc(cliente);
    }

    public Optional<Agendamento> buscarPorId(Long id) {
        return agendamentoRepository.findById(id);
    }

    public List<Agendamento> listarProximosSete() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime daquiSeteDias = agora.plusDays(7);
        return agendamentoRepository.findByDataHoraBetweenOrderByDataHora(agora, daquiSeteDias);
    }

    public void deletar(Long id) {
        agendamentoRepository.deleteById(id);
    }

    public List<Agendamento> listarPorDia(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim    = data.atTime(23, 59, 59);
        return agendamentoRepository.findByDataHoraBetweenOrderByDataHora(inicio, fim);
    }

    // retorna os horários já ocupados no dia — leva em conta a duração dos serviços
    // para bloquear slots de 30min enquanto o atendimento estiver em andamento
    public List<String> buscarHorariosOcupados(LocalDate data) {
        LocalDateTime inicio = data.atTime(9, 0);
        LocalDateTime fim = data.atTime(18, 0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        List<Agendamento> agendamentos = agendamentoRepository
            .findByDataHoraBetweenOrderByDataHora(inicio, fim);

        Set<String> ocupados = new LinkedHashSet<>();

        for (Agendamento ag : agendamentos) {
            if (ag.getStatus() == StatusAgendamento.CANCELADO) continue;

            int duracaoTotal = ag.getServicos().stream()
                .mapToInt(s -> s.getDuracaoMinutos())
                .sum();
            if (duracaoTotal == 0) duracaoTotal = 30;

            LocalDateTime slot = ag.getDataHora();
            LocalDateTime slotFim = ag.getDataHora().plusMinutes(duracaoTotal);
            while (slot.isBefore(slotFim)) {
                ocupados.add(slot.format(fmt));
                slot = slot.plusMinutes(30);
            }
        }

        return new ArrayList<>(ocupados);
    }
}
