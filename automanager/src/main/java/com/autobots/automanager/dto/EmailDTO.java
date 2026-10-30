package com.autobots.automanager.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.Vinculo;

import lombok.Data;

@Data
public class EmailDTO {

    @NotBlank(groups = Criacao.class, message = "O endereço de e-mail deve ser informado")
    @Email(message = "O endereço de e-mail é inválido")
    private String endereco;

    @NotNull(groups = Vinculo.class, message = "O usuário dono do e-mail deve ser informado")
    private Long usuarioId;

    public com.autobots.automanager.entidades.Email paraEntidade() {
        com.autobots.automanager.entidades.Email email = new com.autobots.automanager.entidades.Email();
        email.setEndereco(endereco);
        return email;
    }
}
