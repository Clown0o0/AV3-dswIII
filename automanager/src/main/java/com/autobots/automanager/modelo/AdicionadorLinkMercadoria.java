package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Mercadoria;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(Mercadoria mercadoria) {
        if (mercadoria.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = mercadoria.getId();
        mercadoria.add(linkTo(methodOn(MercadoriaControle.class).buscar(id)).withSelfRel());
        mercadoria.add(linkTo(methodOn(MercadoriaControle.class).listar()).withRel("mercadorias"));
        mercadoria.add(linkTo(methodOn(MercadoriaControle.class).atualizar(id, null)).withRel("atualizar"));
        mercadoria.add(linkTo(methodOn(MercadoriaControle.class).deletar(id)).withRel("excluir"));
        if (mercadoria.getEmpresaId() != null) {
            mercadoria.add(linkTo(methodOn(EmpresaControle.class).buscar(mercadoria.getEmpresaId())).withRel("empresa"));
        }
        if (mercadoria.getFornecedorId() != null) {
            mercadoria.add(linkTo(methodOn(UsuarioControle.class).buscar(mercadoria.getFornecedorId())).withRel("fornecedor"));
        }
    }
}
