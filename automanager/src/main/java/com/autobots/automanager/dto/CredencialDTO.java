package com.autobots.automanager.dto;

import javax.validation.constraints.NotNull;

import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Credencial;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CredencialUsuarioSenhaDTO.class, name = "usuarioSenha"),
        @JsonSubTypes.Type(value = CredencialCodigoBarraDTO.class, name = "codigoBarra") })
public abstract class CredencialDTO {

    private Boolean inativo;

    @NotNull(groups = Vinculo.class, message = "O usuário dono da credencial deve ser informado")
    private Long usuarioId;

    public abstract Credencial paraEntidade();
}
