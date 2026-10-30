package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.entidades.Endereco;

@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco> {

    @Override
    public void adicionarLink(Endereco endereco) {
        if (endereco == null || endereco.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = endereco.getId();
        endereco.add(linkTo(methodOn(EnderecoControle.class).buscar(id)).withSelfRel());
        endereco.add(linkTo(methodOn(EnderecoControle.class).listar()).withRel("enderecos"));
        endereco.add(linkTo(methodOn(EnderecoControle.class).atualizar(id, null)).withRel("atualizar"));
        endereco.add(linkTo(methodOn(EnderecoControle.class).deletar(id)).withRel("excluir"));
    }
}
