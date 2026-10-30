package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {

    @Autowired
    private AdicionadorLinkTelefone adicionadorTelefone;

    @Autowired
    private AdicionadorLinkEndereco adicionadorEndereco;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorMercadoria;

    @Autowired
    private AdicionadorLinkServico adicionadorServico;

    @Override
    public void adicionarLink(Empresa empresa) {
        if (empresa.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = empresa.getId();
        empresa.add(linkTo(methodOn(EmpresaControle.class).buscar(id)).withSelfRel());
        empresa.add(linkTo(methodOn(EmpresaControle.class).listar()).withRel("empresas"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).atualizar(id, null)).withRel("atualizar"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).deletar(id)).withRel("excluir"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).usuarios(id)).withRel("usuarios"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).criarUsuario(id, null)).withRel("novo-usuario"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).mercadorias(id)).withRel("mercadorias"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).servicos(id)).withRel("servicos"));
        empresa.add(linkTo(methodOn(EmpresaControle.class).vendas(id)).withRel("vendas"));
        adicionadorTelefone.adicionarLink(empresa.getTelefones());
        adicionadorEndereco.adicionarLink(empresa.getEndereco());
        adicionadorMercadoria.adicionarLink(empresa.getMercadorias());
        adicionadorServico.adicionarLink(empresa.getServicos());
    }
}
