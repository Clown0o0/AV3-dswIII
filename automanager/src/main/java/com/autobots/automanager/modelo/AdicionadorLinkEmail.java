package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.entidades.Email;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<Email> {

    @Override
    public void adicionarLink(Email email) {
        if (email.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = email.getId();
        email.add(linkTo(methodOn(EmailControle.class).buscar(id)).withSelfRel());
        email.add(linkTo(methodOn(EmailControle.class).listar()).withRel("emails"));
        email.add(linkTo(methodOn(EmailControle.class).atualizar(id, null)).withRel("atualizar"));
        email.add(linkTo(methodOn(EmailControle.class).deletar(id)).withRel("excluir"));
    }
}
