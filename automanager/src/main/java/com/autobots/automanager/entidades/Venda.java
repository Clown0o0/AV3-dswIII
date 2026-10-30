package com.autobots.automanager.entidades;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "vendas")
public class Venda extends Entidade<Venda> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private Date cadastro;

    @Column(nullable = false, unique = true)
    private String identificacao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario cliente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario funcionario;

    @OrderBy("id")
    @ManyToMany
    @JoinTable(name = "venda_mercadorias")
    private Set<Mercadoria> mercadorias = new LinkedHashSet<>();

    @OrderBy("id")
    @ManyToMany
    @JoinTable(name = "venda_servicos")
    private Set<Servico> servicos = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Veiculo veiculo;

    @JsonIgnore
    @Column(name = "empresa_id", insertable = false, updatable = false)
    private Long empresaId;
}
