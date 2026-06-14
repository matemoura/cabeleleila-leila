package com.dsin.salaoleila.controller;

import com.dsin.salaoleila.model.ItemEstoque;
import com.dsin.salaoleila.model.enums.CategoriaItem;
import com.dsin.salaoleila.service.ItemEstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private ItemEstoqueService estoqueService;

    @GetMapping
    public String lista(Model model) {
        var itens = estoqueService.listarTodos();
        long abaixoMinimo = itens.stream().filter(ItemEstoque::isAbaixoDoMinimo).count();
        long semEstoque   = itens.stream().filter(ItemEstoque::isSemEstoque).count();

        model.addAttribute("itens", itens);
        model.addAttribute("totalItens", itens.size());
        model.addAttribute("abaixoMinimo", abaixoMinimo);
        model.addAttribute("semEstoque", semEstoque);
        model.addAttribute("categorias", CategoriaItem.values());
        return "estoque/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("item", new ItemEstoque());
        model.addAttribute("categorias", CategoriaItem.values());
        return "estoque/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ItemEstoque item = estoqueService.buscarPorId(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("categorias", CategoriaItem.values());
        return "estoque/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("item") ItemEstoque item,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", CategoriaItem.values());
            return "estoque/formulario";
        }
        estoqueService.salvar(item);
        redirectAttributes.addFlashAttribute("sucesso", "Item salvo com sucesso!");
        return "redirect:/estoque";
    }

    // Math.max(0, quantidade) evita que o estoque vá pra negativo
    @PostMapping("/ajustar/{id}")
    public String ajustar(@PathVariable Long id,
                          @RequestParam Integer quantidade,
                          RedirectAttributes redirectAttributes) {
        estoqueService.buscarPorId(id).ifPresent(item -> {
            item.setQuantidade(Math.max(0, quantidade));
            estoqueService.salvar(item);
        });
        redirectAttributes.addFlashAttribute("sucesso", "Estoque atualizado!");
        return "redirect:/estoque";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        estoqueService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Item removido do estoque.");
        return "redirect:/estoque";
    }
}
