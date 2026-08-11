package com.marcelogomes.concessionaria.dto;

import com.marcelogomes.concessionaria.entity.StatusCarro;
import java.math.BigDecimal;

public record CarroSaidaDTO(
        Long id,
        String modelo,
        String marca,
        Integer anoFabricacao,
        Integer anoModelo,
        String cor,
        String placa,
        String chassi,
        Integer quilometragem,
        BigDecimal preco,
        StatusCarro status
) {}
