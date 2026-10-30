package com.autobots.automanager.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;

import lombok.Data;

@Data
public class VendaDTO {

    @NotBlank(groups = Criacao.class, message = "A identificação da venda deve ser informada")
    @Size(max = 40, message = "A identificação deve ter no máximo 40 caracteres")
    private String identificacao;

    @NotNull(groups = Criacao.class, message = "A empresa da venda deve ser informada")
    private Long empresaId;

    @NotNull(groups = Criacao.class, message = "O cliente da venda deve ser informado")
    private Long clienteId;

    @NotNull(groups = Criacao.class, message = "O funcionário da venda deve ser informado")
    private Long funcionarioId;

    private Long veiculoId;

    private List<@NotNull(message = "Id de mercadoria inválido") Long> mercadorias;

    private List<@NotNull(message = "Id de serviço inválido") Long> servicos;
}
