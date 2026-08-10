package com.marcelogomes.concessionaria.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "carros")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String marca;

    // Ano em que o veículo foi fabricado fisicamente
    @Column(name = "ano_fabricacao", nullable = false)
    private Integer anoFabricacao;

    // Ano do modelo (ex: Corolla 2024 fabricado em 2023)
    @Column(name = "ano_modelo", nullable = false)
    private Integer anoModelo;

    // Cor: texto livre — marcas diferentes nomeiam cores de formas distintas
    @Column(nullable = false)
    private String cor;

    // Placa: nullable pois carro zero pode chegar sem placa antes do emplacamento
    @Column(unique = true)
    private String placa;

    // Chassi: sempre presente, nunca repete (mesmo carro zero tem chassi)
    @Column(nullable = false, unique = true)
    private String chassi;

    // Quilometragem: 0 para carros novos, nunca nulo
    @Column(nullable = false)
    private Integer quilometragem;

    // BigDecimal para dinheiro — evita erros de arredondamento de double/float
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    // Status com enum — os três estados são fixos e bem definidos no case
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCarro status;

    public Carro() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }

    public Integer getAnoModelo() { return anoModelo; }
    public void setAnoModelo(Integer anoModelo) { this.anoModelo = anoModelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }

    public Integer getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Integer quilometragem) { this.quilometragem = quilometragem; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public StatusCarro getStatus() { return status; }
    public void setStatus(StatusCarro status) { this.status = status; }
}
