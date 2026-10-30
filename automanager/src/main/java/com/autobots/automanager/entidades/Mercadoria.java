package com.autobots.automanager.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "mercadorias")
public class Mercadoria extends Entidade<Mercadoria> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date validade;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date fabricao;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private Date cadastro;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private long quantidade;

    @Column(nullable = false)
    private double valor;

    @Column
    private String descricao;

    @JsonIgnore
    @Column(name = "empresa_id", insertable = false, updatable = false)
    private Long empresaId;

    @JsonIgnore
    @Column(name = "fornecedor_id", insertable = false, updatable = false)
    private Long fornecedorId;
}
