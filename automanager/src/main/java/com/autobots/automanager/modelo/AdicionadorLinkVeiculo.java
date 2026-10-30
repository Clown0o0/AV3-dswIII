package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entidades.Veiculo;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {

    @Override
    public void adicionarLink(Veiculo veiculo) {
        if (veiculo.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = veiculo.getId();
        veiculo.add(linkTo(methodOn(VeiculoControle.class).buscar(id)).withSelfRel());
        veiculo.add(linkTo(methodOn(VeiculoControle.class).listar()).withRel("veiculos"));
        veiculo.add(linkTo(methodOn(VeiculoControle.class).atualizar(id, null)).withRel("atualizar"));
        veiculo.add(linkTo(methodOn(VeiculoControle.class).deletar(id)).withRel("excluir"));
        veiculo.add(linkTo(methodOn(UsuarioControle.class).buscar(veiculo.getProprietario().getId())).withRel("proprietario"));
        veiculo.add(linkTo(methodOn(VeiculoControle.class).vendas(id)).withRel("vendas"));
    }
}
