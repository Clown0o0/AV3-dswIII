package com.autobots.automanager.entidades;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "empresas")
public class Empresa extends Entidade<Empresa> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String razaoSocial;

    @Column
    private String nomeFantasia;

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "empresa_id")
    private Set<Telefone> telefones = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private Date cadastro;

    @JsonIgnore
    @OrderBy("id")
    @OneToMany
    @JoinColumn(name = "empresa_id")
    private Set<Usuario> usuarios = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "empresa_id")
    private Set<Mercadoria> mercadorias = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "empresa_id")
    private Set<Servico> servicos = new LinkedHashSet<>();

    @JsonIgnore
    @OrderBy("id")
    @OneToMany
    @JoinColumn(name = "empresa_id")
    private Set<Venda> vendas = new LinkedHashSet<>();
}
