package com.dsin.salaoleila.repository;

import com.dsin.salaoleila.model.ItemEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, Long> {

    List<ItemEstoque> findAllByOrderByNome();

    // busca itens onde a quantidade atual está abaixo do mínimo configurado
    @Query("SELECT i FROM ItemEstoque i WHERE i.quantidade < i.quantidadeMinima ORDER BY i.nome")
    List<ItemEstoque> findItensAbaixoDoMinimo();
}
