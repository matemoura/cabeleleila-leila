package com.dsin.salaoleila.service;

import com.dsin.salaoleila.model.Cliente;
import com.dsin.salaoleila.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public boolean cpfJaCadastrado(String cpf) {
        return clienteRepository.findByCpf(cpf).isPresent();
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
