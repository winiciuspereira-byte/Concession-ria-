package com.marcelogomes.concessionaria.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CarroEntradaDTO(

        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @NotNull(message = "O ano de fabricação é obrigatório")
        @Min(value = 1900, message = "Ano de fabricação inválido")
        @Max(value = 2026, message = "Ano de fabricação não pode ser no futuro")
        Integer anoFabricacao,

        @NotNull(message = "O ano do modelo é obrigatório")
        @Min(value = 1900, message = "Ano do modelo inválido")
        @Max(value = 2027, message = "Ano do modelo não pode ultrapassar o próximo ano")
        Integer anoModelo,

        @NotBlank(message = "A cor é obrigatória")
        String cor,

        String placa,

        @NotBlank(message = "O chassi é obrigatório")
        @Size(min = 17, max = 17, message = "O chassi deve ter exatamente 17 caracteres")
        String chassi,

        @NotNull(message = "A quilometragem é obrigatória")
        @PositiveOrZero(message = "A quilometragem não pode ser negativa")
        Integer quilometragem,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco

) {}
