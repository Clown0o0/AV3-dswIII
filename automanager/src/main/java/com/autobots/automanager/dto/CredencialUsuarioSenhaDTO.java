package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CredencialUsuarioSenhaDTO extends CredencialDTO {

    @NotBlank(groups = Criacao.class, message = "O nome de usuário deve ser informado")
    @Pattern(regexp = "[A-Za-z0-9._-]{3,30}", message = "O nome de usuário deve ter de 3 a 30 letras, números, '.', '_' ou '-'")
    private String nomeUsuario;

    @NotBlank(groups = Criacao.class, message = "A senha deve ser informada")
    @Size(min = 6, max = 64, message = "A senha deve ter de 6 a 64 caracteres")
    @ToString.Exclude
    private String senha;

    @Override
    public CredencialUsuarioSenha paraEntidade() {
        CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
        credencial.setNomeUsuario(nomeUsuario);
        credencial.setSenha(senha);
        return credencial;
    }
}
