package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.Agendamento;
import com.dsin.salaoleila.model.Cliente;
import com.dsin.salaoleila.model.enums.StatusAgendamento;
import com.dsin.salaoleila.service.AgendamentoService;
import com.dsin.salaoleila.service.ClienteService;
import com.dsin.salaoleila.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired private AgendamentoService agendamentoService;
    @Autowired private ClienteService clienteService;
    @Autowired private ServicoService servicoService;

    @GetMapping("/historico/{clienteId}")
    public String historico(@PathVariable Long clienteId, Model model) {
        var clienteOpt = clienteService.buscarPorId(clienteId);
        if (clienteOpt.isEmpty()) {
            return "redirect:/";
        }
        Cliente cliente = clienteOpt.get();
        List<Agendamento> agendamentos = agendamentoService.listarPorCliente(cliente);

        // calcula o total de cada agendamento aqui pra não ter que fazer isso no template
        Map<Long, BigDecimal> totais = agendamentos.stream()
            .collect(Collectors.toMap(
                Agendamento::getId,
                ag -> ag.getServicos().stream()
                        .map(s -> s.getPreco())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
            ));

        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("cliente", cliente);
        model.addAttribute("totais", totais);
        return "agendamentos/historico";
    }

    @GetMapping("/novo/{clienteId}")
    public String formularioNovo(@PathVariable Long clienteId,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
                                  @RequestParam(required = false) Long servicoId,
                                  Model model) {
        Cliente cliente = clienteService.buscarPorId(clienteId).orElseThrow();

        if (dataHora != null) {
            agendamentoService.sugerirData(cliente, dataHora)
                .ifPresent(sugestao -> model.addAttribute("sugestao", sugestao));
        }

        model.addAttribute("agendamento", new Agendamento());
        model.addAttribute("servicos", servicoService.listarTodos());
        model.addAttribute("cliente", cliente);
        // servicoId vem quando o cliente clica em "Agendar" direto na home
        model.addAttribute("servicoPreSelecionado", servicoId);
        return "agendamentos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Agendamento agendamento,
                          @RequestParam Long clienteId,
                          @RequestParam(required = false) List<Long> servicosIds,
                          RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.buscarPorId(clienteId).orElseThrow();
        agendamento.setCliente(cliente);

        if (servicosIds != null && !servicosIds.isEmpty()) {
            agendamento.setServicos(servicoService.buscarPorIds(servicosIds));
        }

        agendamentoService.salvar(agendamento);
        redirectAttributes.addFlashAttribute("sucesso", "Agendamento realizado com sucesso!");
        return "redirect:/agendamentos/historico/" + clienteId;
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model,
                                    RedirectAttributes redirectAttributes) {
        Agendamento agendamento = agendamentoService.buscarPorId(id).orElseThrow();

        if (!agendamentoService.podeEditar(agendamento)) {
            redirectAttributes.addFlashAttribute("erro",
                "Não é possível editar. Faltam menos de 2 dias. Entre em contato por telefone: (71) 99999-9999");
            return "redirect:/agendamentos/historico/" + agendamento.getCliente().getId();
        }

        model.addAttribute("agendamento", agendamento);
        model.addAttribute("servicos", servicoService.listarTodos());
        model.addAttribute("cliente", agendamento.getCliente());
        return "agendamentos/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute Agendamento agendamento,
                             @RequestParam Long clienteId,
                             @RequestParam(required = false) List<Long> servicosIds,
                             RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.buscarPorId(clienteId).orElseThrow();
        agendamento.setCliente(cliente);

        if (servicosIds != null && !servicosIds.isEmpty()) {
            agendamento.setServicos(servicoService.buscarPorIds(servicosIds));
        }

        try {
            agendamentoService.atualizar(agendamento);
            redirectAttributes.addFlashAttribute("sucesso", "Agendamento atualizado com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/agendamentos/historico/" + clienteId;
    }

    @GetMapping("/detalhe/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Agendamento agendamento = agendamentoService.buscarPorId(id).orElseThrow();
        model.addAttribute("agendamento", agendamento);
        return "agendamentos/detalhe";
    }

    // endpoint usado pelo calendário via fetch para checar horários disponíveis
    @GetMapping("/disponibilidade")
    @ResponseBody
    public List<String> disponibilidade(@RequestParam String data) {
        LocalDate localDate = LocalDate.parse(data);
        return agendamentoService.buscarHorariosOcupados(localDate);
    }
}
