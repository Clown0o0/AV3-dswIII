package com.autobots.automanager.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.hateoas.server.core.Relation;

import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "documentos")
public class Documento extends Entidade<Documento> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipo;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date dataEmissao;

    @Column(unique = true, nullable = false)
    private String numero;
}
