package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.model.Funcionario;
import com.dsin.salaoleila.model.enums.CargoFuncionario;
import com.dsin.salaoleila.model.enums.StatusAgendamento;
import com.dsin.salaoleila.service.AgendamentoService;
import com.dsin.salaoleila.service.FuncionarioService;
import com.dsin.salaoleila.service.ItemEstoqueService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/painel")
public class PainelController {

    @Autowired private AgendamentoService agendamentoService;
    @Autowired private ItemEstoqueService estoqueService;
    @Autowired private FuncionarioService funcionarioService;

    @GetMapping
    public String painel(HttpSession session, Model model) {
        Long funcionarioId = (Long) session.getAttribute("funcionarioId");
        if (funcionarioId == null) {
            return "redirect:/funcionarios/login";
        }

        // painel só abre pra gerente
        Funcionario leila = funcionarioService.buscarPorId(funcionarioId).orElse(null);
        if (leila == null || leila.getCargo() != CargoFuncionario.GERENTE) {
            return "redirect:/funcionarios/login";
        }

        List<Agendamento> agendamentos = agendamentoService.listarProximosSete();

        long confirmados = agendamentos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO).count();
        long pendentes = agendamentos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.PENDENTE).count();

        BigDecimal receitaPrevista = agendamentos.stream()
            .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
            .flatMap(a -> a.getServicos().stream())
            .map(s -> s.getPreco())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        var itensAbaixoDoMinimo = estoqueService.listarAbaixoDoMinimo();

        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("total", agendamentos.size());
        model.addAttribute("confirmados", confirmados);
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("receitaPrevista", receitaPrevista);
        model.addAttribute("itensAbaixoDoMinimo", itensAbaixoDoMinimo);
        return "painel/index";
    }

    @PostMapping("/confirmar/{id}")
    public String confirmar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("funcionarioId") == null) return "redirect:/funcionarios/login";
        agendamentoService.buscarPorId(id).ifPresent(ag -> {
            ag.setStatus(StatusAgendamento.CONFIRMADO);
            agendamentoService.salvar(ag);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Agendamento confirmado!");
        return "redirect:/painel";
    }

    @PostMapping("/finalizar/{id}")
    public String finalizar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("funcionarioId") == null) return "redirect:/funcionarios/login";
        agendamentoService.buscarPorId(id).ifPresent(ag -> {
            ag.setStatus(StatusAgendamento.CONCLUIDO);
            agendamentoService.salvar(ag);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Atendimento finalizado!");
        return "redirect:/painel";
    }

    @PostMapping("/remarcar/{id}")
    public String remarcar(@PathVariable Long id,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime novaDataHora,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (session.getAttribute("funcionarioId") == null) return "redirect:/funcionarios/login";
        agendamentoService.buscarPorId(id).ifPresent(ag -> {
            ag.setDataHora(novaDataHora);
            agendamentoService.salvar(ag);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Agendamento remarcado!");
        return "redirect:/painel";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("funcionarioId") == null) return "redirect:/funcionarios/login";
        agendamentoService.buscarPorId(id).ifPresent(ag -> {
            ag.setStatus(StatusAgendamento.CANCELADO);
            agendamentoService.salvar(ag);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Agendamento cancelado.");
        return "redirect:/painel";
    }
}
