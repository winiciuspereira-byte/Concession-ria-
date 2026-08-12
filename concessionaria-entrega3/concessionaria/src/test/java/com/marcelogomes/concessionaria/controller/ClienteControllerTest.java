package com.marcelogomes.concessionaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelogomes.concessionaria.dto.ClienteEntradaDTO;
import com.marcelogomes.concessionaria.dto.ClienteSaidaDTO;
import com.marcelogomes.concessionaria.exception.ConflitoUnicidadeException;
import com.marcelogomes.concessionaria.exception.RecursoNaoEncontradoException;
import com.marcelogomes.concessionaria.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService service;

    @Test
    void cadastrar_deveRetornar201_quandoDadosValidos() throws Exception {
        ClienteEntradaDTO entrada = new ClienteEntradaDTO(
                "Ana Silva", "123.456.789-00", "11999990000", "ana@email.com"
        );
        ClienteSaidaDTO saida = new ClienteSaidaDTO(1L, "Ana Silva", "11999990000", "ana@email.com");

        when(service.cadastrar(any())).thenReturn(saida);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Ana Silva"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoCpfComLetra() throws Exception {
        String json = """
                {
                  "nome": "Ana Silva",
                  "cpf": "ABC.456.789-00",
                  "telefone": "11999990000",
                  "email": "ana@email.com"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erros[0].campo").value("cpf"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoEmailInvalido() throws Exception {
        String json = """
                {
                  "nome": "Ana Silva",
                  "cpf": "123.456.789-00",
                  "telefone": "11999990000",
                  "email": "nao-e-um-email"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("email"));
    }

    @Test
    void cadastrar_deveRetornar400_quandoNomeEmBranco() throws Exception {
        String json = """
                {
                  "nome": "",
                  "cpf": "123.456.789-00",
                  "telefone": "11999990000",
                  "email": "ana@email.com"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("nome"));
    }

    @Test
    void cadastrar_deveRetornar409_quandoCpfDuplicado() throws Exception {
        ClienteEntradaDTO entrada = new ClienteEntradaDTO(
                "Ana Silva", "123.456.789-00", "11999990000", "ana@email.com"
        );

        when(service.cadastrar(any())).thenThrow(new ConflitoUnicidadeException("CPF já cadastrado"));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void listar_deveRetornar200() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new ClienteSaidaDTO(1L, "Ana Silva", "11999990000", "ana@email.com")
        ));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ana Silva"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoNaoExiste() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Cliente não encontrado com id 99"));

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
