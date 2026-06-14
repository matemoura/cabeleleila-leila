package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.service.AgendamentoService;
import com.dsin.salaoleila.service.ClienteService;
import com.dsin.salaoleila.service.ServicoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired private ServicoService servicoService;
    @Autowired private ClienteService clienteService;
    @Autowired private AgendamentoService agendamentoService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("servicos", servicoService.listarTodos());

        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId != null) {
            clienteService.buscarPorId(clienteId).ifPresentOrElse(cliente -> {
                model.addAttribute("clienteLogado", cliente);
                List<Agendamento> todos = agendamentoService.listarPorCliente(cliente);
                List<Agendamento> proximos = todos.stream()
                    .filter(a -> a.getDataHora().isAfter(LocalDateTime.now()))
                    .limit(3)
                    .collect(Collectors.toList());
                model.addAttribute("proximosAgendamentos", proximos);
            }, () -> session.removeAttribute("clienteId"));
        }

        return "home";
    }

    @GetMapping("/agendar")
    public String agendar(@org.springframework.web.bind.annotation.RequestParam(required = false) Long servicoId,
                          HttpSession session, RedirectAttributes redirectAttributes) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            // preserva o servicoId na URL de retorno pra manter o serviço selecionado após login
            String destino = servicoId != null ? "/agendar?servicoId=" + servicoId : "/agendar";
            redirectAttributes.addFlashAttribute("aviso",
                "Faça login para finalizar seu agendamento.");
            return "redirect:/clientes/login?redirect=" +
                   URLEncoder.encode(destino, StandardCharsets.UTF_8);
        }
        String url = "/agendamentos/novo/" + clienteId;
        if (servicoId != null) url += "?servicoId=" + servicoId;
        return "redirect:" + url;
    }
}
