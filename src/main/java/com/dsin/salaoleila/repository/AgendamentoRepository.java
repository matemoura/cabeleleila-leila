package com.dsin.salaoleila.repository;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByClienteOrderByDataHoraDesc(Cliente cliente);

    List<Agendamento> findByClienteAndDataHoraBetween(
        Cliente cliente,
        LocalDateTime inicio,
        LocalDateTime fim
    );

    List<Agendamento> findByDataHoraBetweenOrderByDataHora(
        LocalDateTime inicio,
        LocalDateTime fim
    );
}
