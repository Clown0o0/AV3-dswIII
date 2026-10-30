package com.autobots.automanager.entidades;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.springframework.hateoas.server.core.Relation;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Relation(collectionRelation = "usuarios")
public class Usuario extends Entidade<Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String nomeSocial;

    @OrderBy
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<PerfilUsuario> perfis = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Set<Telefone> telefones = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Set<Documento> documentos = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Set<Email> emails = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Set<Credencial> credenciais = new LinkedHashSet<>();

    @OrderBy("id")
    @OneToMany
    @JoinColumn(name = "fornecedor_id")
    private Set<Mercadoria> mercadorias = new LinkedHashSet<>();

    @JsonIgnore
    @OrderBy("id")
    @ManyToMany
    @JoinTable(name = "usuario_vendas")
    private Set<Venda> vendas = new LinkedHashSet<>();

    @JsonIgnore
    @OrderBy("id")
    @OneToMany(mappedBy = "proprietario")
    private Set<Veiculo> veiculos = new LinkedHashSet<>();

    @JsonIgnore
    @Column(name = "empresa_id", insertable = false, updatable = false)
    private Long empresaId;
}
