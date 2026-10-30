package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Credencial;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<Credencial> {

    @Override
    public void adicionarLink(Credencial credencial) {
        if (credencial.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = credencial.getId();
        credencial.add(linkTo(methodOn(CredencialControle.class).buscar(id)).withSelfRel());
        credencial.add(linkTo(methodOn(CredencialControle.class).listar()).withRel("credenciais"));
        credencial.add(linkTo(methodOn(CredencialControle.class).atualizar(id, null)).withRel("atualizar"));
        credencial.add(linkTo(methodOn(CredencialControle.class).deletar(id)).withRel("excluir"));
        if (credencial.getUsuarioId() != null) {
            credencial.add(linkTo(methodOn(UsuarioControle.class).buscar(credencial.getUsuarioId())).withRel("usuario"));
        }
    }
}
