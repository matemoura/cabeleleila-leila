package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.Cliente;
import com.dsin.salaoleila.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/login")
    public String formLogin(@RequestParam(required = false) String redirect,
                            Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("redirectUrl", redirect != null ? redirect : "/");
        return "clientes/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String cpf,
                        @RequestParam(required = false, defaultValue = "/") String redirectUrl,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // remove formatação do CPF antes de buscar
        String cpfLimpo = cpf.replaceAll("[^\\d]", "");
        return clienteService.buscarPorCpf(cpfLimpo)
            .or(() -> clienteService.buscarPorCpf(cpf))
            .map(c -> {
                session.setAttribute("clienteId", c.getId());
                return "redirect:" + redirectUrl;
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("erro", "CPF não encontrado. Cadastre-se primeiro.");
                return "redirect:/clientes/login?redirect=" + redirectUrl;
            });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Cliente cliente,
                         BindingResult result,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (clienteService.cpfJaCadastrado(cliente.getCpf())) {
            result.rejectValue("cpf", "cpf.duplicado", "Este CPF já está cadastrado.");
        }
        if (result.hasErrors()) {
            return "clientes/formulario";
        }
        Cliente salvo = clienteService.salvar(cliente);
        session.setAttribute("clienteId", salvo.getId());
        redirectAttributes.addFlashAttribute("sucesso", "Cadastro realizado! Bem-vinda, " + salvo.getNome() + "!");
        return "redirect:/";
    }
}
