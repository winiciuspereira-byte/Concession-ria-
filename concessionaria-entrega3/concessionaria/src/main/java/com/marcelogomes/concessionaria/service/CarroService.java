package com.marcelogomes.concessionaria.service;

import com.marcelogomes.concessionaria.dto.CarroEntradaDTO;
import com.marcelogomes.concessionaria.dto.CarroSaidaDTO;
import com.marcelogomes.concessionaria.entity.Carro;
import com.marcelogomes.concessionaria.entity.StatusCarro;
import com.marcelogomes.concessionaria.exception.ConflitoUnicidadeException;
import com.marcelogomes.concessionaria.exception.RecursoNaoEncontradoException;
import com.marcelogomes.concessionaria.repository.CarroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarroService {

    private final CarroRepository repository;

    public CarroService(CarroRepository repository) {
        this.repository = repository;
    }

    public CarroSaidaDTO cadastrar(CarroEntradaDTO dto) {
        if (repository.existsByChassi(dto.chassi())) {
            throw new ConflitoUnicidadeException("Chassi " + dto.chassi() + " já está cadastrado");
        }
        if (dto.placa() != null && !dto.placa().isBlank() && repository.existsByPlaca(dto.placa())) {
            throw new ConflitoUnicidadeException("Placa " + dto.placa() + " já está cadastrada");
        }

        Carro carro = new Carro();
        carro.setModelo(dto.modelo());
        carro.setMarca(dto.marca());
        carro.setAnoFabricacao(dto.anoFabricacao());
        carro.setAnoModelo(dto.anoModelo());
        carro.setCor(dto.cor());
        carro.setPlaca(dto.placa());
        carro.setChassi(dto.chassi());
        carro.setQuilometragem(dto.quilometragem());
        carro.setPreco(dto.preco());
        carro.setStatus(StatusCarro.DISPONIVEL);

        return toSaida(repository.save(carro));
    }

    public List<CarroSaidaDTO> listar(String cor, Integer ano) {
        return repository.filtrar(cor, ano).stream()
                .map(this::toSaida)
                .toList();
    }

    public CarroSaidaDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toSaida)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carro não encontrado com id " + id));
    }

    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Carro não encontrado com id " + id);
        }
        repository.deleteById(id);
    }

    private CarroSaidaDTO toSaida(Carro c) {
        return new CarroSaidaDTO(
                c.getId(), c.getModelo(), c.getMarca(),
                c.getAnoFabricacao(), c.getAnoModelo(), c.getCor(),
                c.getPlaca(), c.getChassi(), c.getQuilometragem(),
                c.getPreco(), c.getStatus()
        );
    }
}
