package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.model.Funcionario;
import com.dsin.salaoleila.model.enums.StatusAgendamento;
import com.dsin.salaoleila.service.AgendamentoService;
import com.dsin.salaoleila.service.FuncionarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired private AgendamentoService agendamentoService;
    @Autowired private FuncionarioService funcionarioService;

    @GetMapping
    public String selecionar(
            @RequestParam(required = false) Long funcionarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            HttpSession session,
            Model model) {

        if (funcionarioId == null) {
            Long sessionId = (Long) session.getAttribute("funcionarioId");
            if (sessionId == null) {
                return "redirect:/funcionarios/login";
            }
            return "redirect:/agenda?funcionarioId=" + sessionId;
        }

        Funcionario funcionario = funcionarioService.buscarPorId(funcionarioId).orElseThrow();
        if (data == null) data = LocalDate.now();

        // filtra só os agendamentos que têm serviços que essa funcionária atende
        Set<Long> servicosDoFuncionario = funcionario.getServicos().stream()
            .map(s -> s.getId()).collect(Collectors.toSet());

        List<Agendamento> todos = agendamentoService.listarPorDia(data);

        List<Agendamento> agendamentos = servicosDoFuncionario.isEmpty() ? todos :
            todos.stream()
                .filter(a -> a.getServicos().stream()
                    .anyMatch(s -> servicosDoFuncionario.contains(s.getId())))
                .collect(Collectors.toList());

        long total      = agendamentos.stream().filter(a -> a.getStatus() != StatusAgendamento.CANCELADO).count();
        long concluidos = agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO).count();
        long pendentes  = agendamentos.stream().filter(a ->
            a.getStatus() == StatusAgendamento.PENDENTE ||
            a.getStatus() == StatusAgendamento.CONFIRMADO).count();

        BigDecimal receitaDia = agendamentos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
            .flatMap(a -> a.getServicos().stream())
            .map(s -> s.getPreco())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("funcionario", funcionario);
        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("data", data);
        model.addAttribute("dataAnterior", data.minusDays(1));
        model.addAttribute("dataProxima", data.plusDays(1));
        model.addAttribute("isHoje", data.equals(LocalDate.now()));
        model.addAttribute("total", total);
        model.addAttribute("concluidos", concluidos);
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("receitaDia", receitaDia);

        return "funcionarios/agenda";
    }

    @PostMapping("/concluir/{id}")
    public String concluir(@PathVariable Long id,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                           @RequestParam Long funcionarioId,
                           RedirectAttributes redirectAttributes) {
        agendamentoService.buscarPorId(id).ifPresent(ag -> {
            ag.setStatus(StatusAgendamento.CONCLUIDO);
            agendamentoService.salvar(ag);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Atendimento concluido!");
        return "redirect:/agenda?funcionarioId=" + funcionarioId + "&data=" + data;
    }

    @PostMapping("/iniciar/{id}")
    public String iniciar(@PathVariable Long id,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                          @RequestParam Long funcionarioId,
                          RedirectAttributes redirectAttributes) {
        agendamentoService.buscarPorId(id).ifPresent(ag -> {
            ag.setStatus(StatusAgendamento.CONFIRMADO);
            agendamentoService.salvar(ag);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Atendimento confirmado!");
        return "redirect:/agenda?funcionarioId=" + funcionarioId + "&data=" + data;
    }
}
