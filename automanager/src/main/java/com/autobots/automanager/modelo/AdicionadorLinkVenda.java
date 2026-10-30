package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entidades.Venda;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {

    @Autowired
    private AdicionadorLinkMercadoria adicionadorMercadoria;

    @Autowired
    private AdicionadorLinkServico adicionadorServico;

    @Override
    public void adicionarLink(Venda venda) {
        if (venda.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = venda.getId();
        venda.add(linkTo(methodOn(VendaControle.class).buscar(id)).withSelfRel());
        venda.add(linkTo(methodOn(VendaControle.class).listar()).withRel("vendas"));
        venda.add(linkTo(methodOn(VendaControle.class).atualizar(id, null)).withRel("atualizar"));
        venda.add(linkTo(methodOn(VendaControle.class).deletar(id)).withRel("excluir"));
        venda.add(linkTo(methodOn(UsuarioControle.class).buscar(venda.getCliente().getId())).withRel("cliente"));
        venda.add(linkTo(methodOn(UsuarioControle.class).buscar(venda.getFuncionario().getId())).withRel("funcionario"));
        if (venda.getVeiculo() != null) {
            venda.add(linkTo(methodOn(VeiculoControle.class).buscar(venda.getVeiculo().getId())).withRel("veiculo"));
        }
        if (venda.getEmpresaId() != null) {
            venda.add(linkTo(methodOn(EmpresaControle.class).buscar(venda.getEmpresaId())).withRel("empresa"));
        }
        adicionadorMercadoria.adicionarLink(venda.getMercadorias());
        adicionadorServico.adicionarLink(venda.getServicos());
    }
}
