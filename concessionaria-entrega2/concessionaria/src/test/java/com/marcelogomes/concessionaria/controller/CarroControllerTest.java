package com.marcelogomes.concessionaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelogomes.concessionaria.dto.CarroEntradaDTO;
import com.marcelogomes.concessionaria.dto.CarroSaidaDTO;
import com.marcelogomes.concessionaria.entity.StatusCarro;
import com.marcelogomes.concessionaria.exception.ConflitoUnicidadeException;
import com.marcelogomes.concessionaria.exception.RecursoNaoEncontradoException;
import com.marcelogomes.concessionaria.service.CarroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarroController.class)
class CarroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarroService service;

    private CarroSaidaDTO saidaPadrao() {
        return new CarroSaidaDTO(1L, "Corolla", "Toyota", 2023, 2024,
                "Prata", null, "9BWZZZ377VT004251", 0,
                new BigDecimal("189990.00"), StatusCarro.DISPONIVEL);
    }

    @Test
    void cadastrar_deveRetornar201_quandoDadosValidos() throws Exception {
        CarroEntradaDTO entrada = new CarroEntradaDTO(
                "Corolla", "Toyota", 2023, 2024,
                "Prata", null, "9BWZZZ377VT004251",
                0, new BigDecimal("189990.00")
        );

        when(service.cadastrar(any())).thenReturn(saidaPadrao());

        mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DISPONIVEL"))
                .andExpect(jsonPath("$.modelo").value("Corolla"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoAnoFabricacaoFuturo() throws Exception {
        String json = """
                {
                  "modelo": "Corolla", "marca": "Toyota",
                  "anoFabricacao": 2202, "anoModelo": 2024,
                  "cor": "Prata", "chassi": "9BWZZZ377VT004251",
                  "quilometragem": 0, "preco": 189990.00
                }
                """;

        mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("anoFabricacao"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoPrecoZero() throws Exception {
        String json = """
                {
                  "modelo": "Corolla", "marca": "Toyota",
                  "anoFabricacao": 2023, "anoModelo": 2024,
                  "cor": "Prata", "chassi": "9BWZZZ377VT004251",
                  "quilometragem": 0, "preco": 0
                }
                """;

        mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("preco"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoQuilometragemNegativa() throws Exception {
        String json = """
                {
                  "modelo": "Corolla", "marca": "Toyota",
                  "anoFabricacao": 2023, "anoModelo": 2024,
                  "cor": "Prata", "chassi": "9BWZZZ377VT004251",
                  "quilometragem": -100, "preco": 189990.00
                }
                """;

        mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("quilometragem"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoChassiComTamanhoErrado() throws Exception {
        String json = """
                {
                  "modelo": "Corolla", "marca": "Toyota",
                  "anoFabricacao": 2023, "anoModelo": 2024,
                  "cor": "Prata", "chassi": "CURTO",
                  "quilometragem": 0, "preco": 189990.00
                }
                """;

        mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("chassi"));
    }

    @Test
    void cadastrar_deveRetornar409_quandoChassiDuplicado() throws Exception {
        CarroEntradaDTO entrada = new CarroEntradaDTO(
                "Corolla", "Toyota", 2023, 2024,
                "Prata", null, "9BWZZZ377VT004251",
                0, new BigDecimal("189990.00")
        );

        when(service.cadastrar(any()))
                .thenThrow(new ConflitoUnicidadeException("Chassi já cadastrado"));

        mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void listar_deveRetornar200() throws Exception {
        when(service.listar()).thenReturn(List.of(saidaPadrao()));

        mockMvc.perform(get("/carros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modelo").value("Corolla"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoNaoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Carro não encontrado com id 99"));

        mockMvc.perform(get("/carros/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
