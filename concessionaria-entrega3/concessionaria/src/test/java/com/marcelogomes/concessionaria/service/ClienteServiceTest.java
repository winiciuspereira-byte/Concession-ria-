package com.marcelogomes.concessionaria.service;

import com.marcelogomes.concessionaria.dto.ClienteEntradaDTO;
import com.marcelogomes.concessionaria.dto.ClienteSaidaDTO;
import com.marcelogomes.concessionaria.entity.Cliente;
import com.marcelogomes.concessionaria.exception.ConflitoUnicidadeException;
import com.marcelogomes.concessionaria.exception.RecursoNaoEncontradoException;
import com.marcelogomes.concessionaria.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private ClienteEntradaDTO dtoValido;
    private Cliente clienteSalvo;

    @BeforeEach
    void setUp() {
        dtoValido = new ClienteEntradaDTO(
                "Ana Silva",
                "123.456.789-00",
                "11999990000",
                "ana@email.com"
        );

        clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("Ana Silva");
        clienteSalvo.setCpf("123.456.789-00");
        clienteSalvo.setTelefone("11999990000");
        clienteSalvo.setEmail("ana@email.com");
    }

    @Test
    void cadastrar_deveRetornarSaidaSemCpf() {
        when(repository.existsByCpf(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(clienteSalvo);

        ClienteSaidaDTO saida = service.cadastrar(dtoValido);

        assertThat(saida.id()).isEqualTo(1L);
        assertThat(saida.nome()).isEqualTo("Ana Silva");
        assertThat(saida.email()).isEqualTo("ana@email.com");
        assertThat(saida).doesNotHaveToString("123.456.789-00");
    }

    @Test
    void cadastrar_deveLancarConflito_quandoCpfJaExiste() {
        when(repository.existsByCpf("123.456.789-00")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar(dtoValido))
                .isInstanceOf(ConflitoUnicidadeException.class)
                .hasMessageContaining("123.456.789-00");
    }

    @Test
    void listar_deveRetornarListaDeClientes() {
        when(repository.findAll()).thenReturn(List.of(clienteSalvo));

        List<ClienteSaidaDTO> lista = service.listar();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).nome()).isEqualTo("Ana Silva");
    }

    @Test
    void buscarPorId_deveRetornarCliente_quandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(clienteSalvo));

        ClienteSaidaDTO saida = service.buscarPorId(1L);

        assertThat(saida.id()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_deveLancar404_quandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void remover_deveLancar404_quandoNaoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.remover(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void remover_deveDeletar_quandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        service.remover(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
