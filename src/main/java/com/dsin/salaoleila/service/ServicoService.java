package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Servico;
import com.dsin.salaoleila.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public List<Servico> buscarPorIds(List<Long> ids) {
        return servicoRepository.findAllById(ids);
    }
}
