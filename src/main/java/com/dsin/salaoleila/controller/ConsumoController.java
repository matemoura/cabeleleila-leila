package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.ItemEstoque;
import com.dsin.salaoleila.model.enums.CategoriaItem;
import com.dsin.salaoleila.service.ItemEstoqueService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/consumo")
public class ConsumoController {

    @Autowired
    private ItemEstoqueService estoqueService;

    @GetMapping
    public String lista(HttpSession session, Model model) {
        if (session.getAttribute("funcionarioId") == null) {
            return "redirect:/funcionarios/login";
        }
        List<ItemEstoque> itens = estoqueService.listarTodos();

        // usa LinkedHashMap pra manter a ordem das categorias igual ao enum
        Map<CategoriaItem, List<ItemEstoque>> porCategoria = new LinkedHashMap<>();
        for (CategoriaItem cat : CategoriaItem.values()) {
            List<ItemEstoque> doGrupo = itens.stream()
                .filter(i -> i.getCategoria() == cat)
                .collect(Collectors.toList());
            if (!doGrupo.isEmpty()) {
                porCategoria.put(cat, doGrupo);
            }
        }

        model.addAttribute("porCategoria", porCategoria);
        model.addAttribute("totalItens", itens.size());
        return "consumo/index";
    }

    @PostMapping("/registrar/{id}")
    public String registrar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("funcionarioId") == null) {
            return "redirect:/funcionarios/login";
        }
        estoqueService.buscarPorId(id).ifPresentOrElse(item -> {
            if (item.getQuantidade() > 0) {
                item.setQuantidade(item.getQuantidade() - 1);
                estoqueService.salvar(item);
                String msg = "Produto \"" + item.getNome() + "\" registrado como consumido. "
                    + "Estoque atual: " + item.getQuantidade() + " " + item.getUnidade() + ".";
                if (item.isAbaixoDoMinimo()) {
                    msg += " ATENCAO: estoque abaixo do minimo!";
                }
                redirectAttributes.addFlashAttribute("sucesso", msg);
                redirectAttributes.addFlashAttribute("alertaBaixo", item.isAbaixoDoMinimo());
            } else {
                redirectAttributes.addFlashAttribute("erro",
                    "\"" + item.getNome() + "\" ja esta com estoque zerado.");
            }
        }, () -> redirectAttributes.addFlashAttribute("erro", "Produto nao encontrado."));

        return "redirect:/consumo";
    }
}
