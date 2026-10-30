package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entidades.Servico;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {

    @Override
    public void adicionarLink(Servico servico) {
        if (servico.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = servico.getId();
        servico.add(linkTo(methodOn(ServicoControle.class).buscar(id)).withSelfRel());
        servico.add(linkTo(methodOn(ServicoControle.class).listar()).withRel("servicos"));
        servico.add(linkTo(methodOn(ServicoControle.class).atualizar(id, null)).withRel("atualizar"));
        servico.add(linkTo(methodOn(ServicoControle.class).deletar(id)).withRel("excluir"));
        if (servico.getEmpresaId() != null) {
            servico.add(linkTo(methodOn(EmpresaControle.class).buscar(servico.getEmpresaId())).withRel("empresa"));
        }
    }
}
