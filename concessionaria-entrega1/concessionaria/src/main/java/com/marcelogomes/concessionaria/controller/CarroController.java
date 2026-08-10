package com.marcelogomes.concessionaria.controller;

import com.marcelogomes.concessionaria.entity.Carro;
import com.marcelogomes.concessionaria.repository.CarroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

    private final CarroRepository repository;

    public CarroController(CarroRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Carro> cadastrar(@RequestBody Carro carro) {
        Carro salvo = repository.save(carro);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping
    public List<Carro> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
