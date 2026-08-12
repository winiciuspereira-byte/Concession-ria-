package com.marcelogomes.concessionaria.controller;

import com.marcelogomes.concessionaria.dto.CarroEntradaDTO;
import com.marcelogomes.concessionaria.dto.CarroSaidaDTO;
import com.marcelogomes.concessionaria.service.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Veículos", description = "Consulta e manutenção do estoque do pátio")
@RestController
@RequestMapping("/carros")
public class CarroController {

    private final CarroService service;

    public CarroController(CarroService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar veículo", description = "Cadastra um novo veículo no estoque. O status começa automaticamente como DISPONIVEL.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ano futuro, preço zero, chassi incorreto etc.)"),
            @ApiResponse(responseCode = "409", description = "Chassi ou placa já cadastrados")
    })
    @PostMapping
    public ResponseEntity<CarroSaidaDTO> cadastrar(@RequestBody @Valid CarroEntradaDTO dto) {
        return ResponseEntity.status(201).body(service.cadastrar(dto));
    }

    @Operation(summary = "Listar veículos", description = "Retorna todos os veículos do estoque. Pode ser filtrado por cor e/ou ano do modelo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping
    public List<CarroSaidaDTO> listar(
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) Integer ano) {
        return service.listar(cor, ano);
    }

    @Operation(summary = "Buscar veículo por ID", description = "Retorna os dados de um veículo específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarroSaidaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Remover veículo", description = "Remove um veículo do estoque pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veículo removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
