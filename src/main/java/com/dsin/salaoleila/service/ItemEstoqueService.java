package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.ItemEstoque;
import com.dsin.salaoleila.repository.ItemEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemEstoqueService {

    @Autowired
    private ItemEstoqueRepository repository;

    public List<ItemEstoque> listarTodos() {
        return repository.findAllByOrderByNome();
    }

    public Optional<ItemEstoque> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public ItemEstoque salvar(ItemEstoque item) {
        return repository.save(item);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public List<ItemEstoque> listarAbaixoDoMinimo() {
        return repository.findItensAbaixoDoMinimo();
    }

    public long countAbaixoDoMinimo() {
        return repository.findItensAbaixoDoMinimo().size();
    }
}
