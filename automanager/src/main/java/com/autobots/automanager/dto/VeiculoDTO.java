package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.Placa;
import com.autobots.automanager.enumeracoes.TipoVeiculo;

import lombok.Data;

@Data
public class VeiculoDTO {

    @NotNull(groups = Criacao.class, message = "O tipo do veículo deve ser informado")
    private TipoVeiculo tipo;

    @NotBlank(groups = Criacao.class, message = "O modelo do veículo deve ser informado")
    @Size(max = 100, message = "O modelo deve ter no máximo 100 caracteres")
    private String modelo;

    @NotBlank(groups = Criacao.class, message = "A placa do veículo deve ser informada")
    @Placa
    private String placa;

    @NotNull(groups = Criacao.class, message = "O proprietário do veículo deve ser informado")
    private Long proprietarioId;
}
