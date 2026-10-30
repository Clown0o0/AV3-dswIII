package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "credenciais")
public class CredencialUsuarioSenha extends Credencial {

    public static final String TIPO = "usuarioSenha";

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha;

    @Override
    public String getTipo() {
        return TIPO;
    }
}
