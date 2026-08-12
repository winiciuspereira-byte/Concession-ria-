package com.marcelogomes.concessionaria.service;

import com.marcelogomes.concessionaria.dto.CarroEntradaDTO;
import com.marcelogomes.concessionaria.dto.CarroSaidaDTO;
import com.marcelogomes.concessionaria.entity.Carro;
import com.marcelogomes.concessionaria.entity.StatusCarro;
import com.marcelogomes.concessionaria.exception.ConflitoUnicidadeException;
import com.marcelogomes.concessionaria.exception.RecursoNaoEncontradoException;
import com.marcelogomes.concessionaria.repository.CarroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository repository;

    @InjectMocks
    private CarroService service;

    private CarroEntradaDTO dtoValido;
    private Carro carroSalvo;

    @BeforeEach
    void setUp() {
        dtoValido = new CarroEntradaDTO(
                "Corolla", "Toyota", 2023, 2024,
                "Prata", null, "9BWZZZ377VT004251",
                0, new BigDecimal("189990.00")
        );

        carroSalvo = new Carro();
        carroSalvo.setId(1L);
        carroSalvo.setModelo("Corolla");
        carroSalvo.setMarca("Toyota");
        carroSalvo.setAnoFabricacao(2023);
        carroSalvo.setAnoModelo(2024);
        carroSalvo.setCor("Prata");
        carroSalvo.setChassi("9BWZZZ377VT004251");
        carroSalvo.setQuilometragem(0);
        carroSalvo.setPreco(new BigDecimal("189990.00"));
        carroSalvo.setStatus(StatusCarro.DISPONIVEL);
    }

    @Test
    void cadastrar_deveDefinirStatusComoDisponivel() {
        when(repository.existsByChassi(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(carroSalvo);

        CarroSaidaDTO saida = service.cadastrar(dtoValido);

        assertThat(saida.status()).isEqualTo(StatusCarro.DISPONIVEL);
    }

    @Test
    void cadastrar_deveLancarConflito_quandoChassiJaExiste() {
        when(repository.existsByChassi("9BWZZZ377VT004251")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar(dtoValido))
                .isInstanceOf(ConflitoUnicidadeException.class)
                .hasMessageContaining("9BWZZZ377VT004251");
    }

    @Test
    void cadastrar_deveLancarConflito_quandoPlacaJaExiste() {
        CarroEntradaDTO dtoComPlaca = new CarroEntradaDTO(
                "Corolla", "Toyota", 2023, 2024,
                "Prata", "ABC1234", "9BWZZZ377VT004251",
                0, new BigDecimal("189990.00")
        );

        when(repository.existsByChassi(any())).thenReturn(false);
        when(repository.existsByPlaca("ABC1234")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrar(dtoComPlaca))
                .isInstanceOf(ConflitoUnicidadeException.class)
                .hasMessageContaining("ABC1234");
    }

    @Test
    void cadastrar_deveAceitarPlacaNula_carroZero() {
        when(repository.existsByChassi(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(carroSalvo);

        assertThatCode(() -> service.cadastrar(dtoValido)).doesNotThrowAnyException();
    }

    @Test
    void listar_deveRetornarListaDeCarros() {
        when(repository.filtrar(null, null)).thenReturn(List.of(carroSalvo));

        List<CarroSaidaDTO> lista = service.listar(null, null);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).modelo()).isEqualTo("Corolla");
    }

    @Test
    void buscarPorId_deveLancar404_quandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void remover_deveDeletar_quandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        service.remover(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void remover_deveLancar404_quandoNaoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.remover(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
