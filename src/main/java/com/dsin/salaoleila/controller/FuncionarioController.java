package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.Funcionario;
import com.dsin.salaoleila.model.enums.CargoFuncionario;
import com.dsin.salaoleila.service.FuncionarioService;
import com.dsin.salaoleila.service.ServicoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/** Controller responsável pelo gerenciamento de funcionárias (CRUD e autenticação). */
@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired private FuncionarioService funcionarioService;
    @Autowired private ServicoService servicoService;

    /** Exibe o formulário de login da área de funcionárias. */
    @GetMapping("/login")
    public String formLogin(Model model) {
        return "funcionarios/login";
    }

    /**
     * Autentica a funcionária pelo login e senha.
     * Gerentes são redirecionadas ao painel; demais funcionárias vão direto para a agenda.
     */
    @PostMapping("/login")
    public String login(@RequestParam String login,
                        @RequestParam String senha,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        return funcionarioService.autenticar(login, senha)
            .map(f -> {
                session.setAttribute("funcionarioId", f.getId());
                if (f.getCargo() == CargoFuncionario.GERENTE) {
                    return "redirect:/painel";
                }
                return "redirect:/agenda?funcionarioId=" + f.getId();
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("erro", "Login ou senha incorretos.");
                return "redirect:/funcionarios/login";
            });
    }

    /** Remove o ID da funcionária da sessão e redireciona para o login. */
    @GetMapping("/sair")
    public String sair(HttpSession session) {
        session.removeAttribute("funcionarioId");
        return "redirect:/funcionarios/login";
    }

    /** Lista todas as funcionárias cadastradas. */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        model.addAttribute("totalAtivos", funcionarioService.listarAtivos().size());
        return "funcionarios/lista";
    }

    /** Exibe o formulário de cadastro de nova funcionária. */
    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("cargos", CargoFuncionario.values());
        model.addAttribute("servicos", servicoService.listarTodos());
        return "funcionarios/formulario";
    }

    /** Exibe o formulário de edição de uma funcionária existente. */
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        Funcionario f = funcionarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("funcionario", f);
        model.addAttribute("cargos", CargoFuncionario.values());
        model.addAttribute("servicos", servicoService.listarTodos());
        return "funcionarios/formulario";
    }

    /** Salva ou atualiza uma funcionária, validando CPF duplicado. */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Funcionario funcionario,
                         BindingResult result,
                         @RequestParam(required = false) List<Long> servicosIds,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (funcionarioService.cpfJaCadastrado(funcionario.getCpf(), funcionario.getId())) {
            result.rejectValue("cpf", "cpf.duplicado", "Este CPF ja esta cadastrado.");
        }
        if (result.hasErrors()) {
            model.addAttribute("cargos", CargoFuncionario.values());
            model.addAttribute("servicos", servicoService.listarTodos());
            return "funcionarios/formulario";
        }
        boolean isNovo = funcionario.getId() == null;
        funcionario.setServicos(
            servicosIds != null ? servicoService.buscarPorIds(servicosIds) : new ArrayList<>()
        );
        funcionarioService.salvar(funcionario);
        String msg = isNovo ? "Funcionario cadastrado!" : "Funcionario atualizado!";
        redirectAttributes.addFlashAttribute("sucesso", msg);
        return "redirect:/funcionarios";
    }

    /** Alterna o status ativo/inativo de uma funcionária. */
    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        funcionarioService.toggleAtivo(id);
        redirectAttributes.addFlashAttribute("sucesso", "Status atualizado.");
        return "redirect:/funcionarios";
    }
}
