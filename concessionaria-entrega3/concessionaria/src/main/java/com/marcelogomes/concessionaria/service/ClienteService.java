package com.marcelogomes.concessionaria.service;

import com.marcelogomes.concessionaria.dto.ClienteEntradaDTO;
import com.marcelogomes.concessionaria.dto.ClienteSaidaDTO;
import com.marcelogomes.concessionaria.entity.Cliente;
import com.marcelogomes.concessionaria.exception.ConflitoUnicidadeException;
import com.marcelogomes.concessionaria.exception.RecursoNaoEncontradoException;
import com.marcelogomes.concessionaria.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteSaidaDTO cadastrar(ClienteEntradaDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new ConflitoUnicidadeException("CPF " + dto.cpf() + " já está cadastrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());

        return toSaida(repository.save(cliente));
    }

    public List<ClienteSaidaDTO> listar() {
        return repository.findAll().stream()
                .map(this::toSaida)
                .toList();
    }

    public ClienteSaidaDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toSaida)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com id " + id));
    }

    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com id " + id);
        }
        repository.deleteById(id);
    }

    private ClienteSaidaDTO toSaida(Cliente c) {
        return new ClienteSaidaDTO(c.getId(), c.getNome(), c.getTelefone(), c.getEmail());
    }
}
