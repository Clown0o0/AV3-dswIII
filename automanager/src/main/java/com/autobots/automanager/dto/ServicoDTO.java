package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;

import lombok.Data;

@Data
public class ServicoDTO {

    @NotBlank(groups = Criacao.class, message = "O nome do serviço deve ser informado")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(groups = Criacao.class, message = "O valor deve ser informado")
    @Positive(message = "O valor deve ser maior que zero")
    private Double valor;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @NotNull(groups = Criacao.class, message = "A empresa do serviço deve ser informada")
    private Long empresaId;
}
