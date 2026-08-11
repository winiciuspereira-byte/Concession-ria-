package com.marcelogomes.concessionaria.controller;

import com.marcelogomes.concessionaria.dto.CarroEntradaDTO;
import com.marcelogomes.concessionaria.dto.CarroSaidaDTO;
import com.marcelogomes.concessionaria.service.CarroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

    private final CarroService service;

    public CarroController(CarroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CarroSaidaDTO> cadastrar(@RequestBody @Valid CarroEntradaDTO dto) {
        return ResponseEntity.status(201).body(service.cadastrar(dto));
    }

    @GetMapping
    public List<CarroSaidaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroSaidaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
