package com.autobots.automanager.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CredencialUsuarioSenha.class, name = CredencialUsuarioSenha.TIPO),
        @JsonSubTypes.Type(value = CredencialCodigoBarra.class, name = CredencialCodigoBarra.TIPO) })
@JsonPropertyOrder({ "tipo", "id" })
@Relation(collectionRelation = "credenciais")
public abstract class Credencial extends Entidade<Credencial> {

    public abstract String getTipo();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private Date criacao;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private Date ultimoAcesso;

    @Column(nullable = false)
    private boolean inativo;

    @JsonIgnore
    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;
}
