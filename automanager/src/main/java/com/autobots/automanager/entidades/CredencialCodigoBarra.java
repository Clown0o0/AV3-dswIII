package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "credenciais")
public class CredencialCodigoBarra extends Credencial {

    public static final String TIPO = "codigoBarra";

    @Column(nullable = false, unique = true)
    private String codigo;

    @Override
    public String getTipo() {
        return TIPO;
    }
}
