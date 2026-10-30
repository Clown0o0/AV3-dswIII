package com.autobots.automanager.modelo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Usuario;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Autowired
    private AdicionadorLinkTelefone adicionadorTelefone;

    @Autowired
    private AdicionadorLinkEndereco adicionadorEndereco;

    @Autowired
    private AdicionadorLinkDocumento adicionadorDocumento;

    @Autowired
    private AdicionadorLinkEmail adicionadorEmail;

    @Autowired
    private AdicionadorLinkCredencial adicionadorCredencial;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorMercadoria;

    @Override
    public void adicionarLink(Usuario usuario) {
        if (usuario.hasLink(IanaLinkRelations.SELF)) {
            return;
        }
        long id = usuario.getId();
        usuario.add(linkTo(methodOn(UsuarioControle.class).buscar(id)).withSelfRel());
        usuario.add(linkTo(methodOn(UsuarioControle.class).listar()).withRel("usuarios"));
        usuario.add(linkTo(methodOn(UsuarioControle.class).atualizar(id, null)).withRel("atualizar"));
        usuario.add(linkTo(methodOn(UsuarioControle.class).deletar(id)).withRel("excluir"));
        if (usuario.getEmpresaId() != null) {
            usuario.add(linkTo(methodOn(EmpresaControle.class).buscar(usuario.getEmpresaId())).withRel("empresa"));
        }
        usuario.add(linkTo(methodOn(UsuarioControle.class).veiculos(id)).withRel("veiculos"));
        usuario.add(linkTo(methodOn(UsuarioControle.class).vendas(id)).withRel("vendas"));
        usuario.add(linkTo(methodOn(UsuarioControle.class).credenciais(id)).withRel("credenciais"));
        adicionadorTelefone.adicionarLink(usuario.getTelefones());
        adicionadorEndereco.adicionarLink(usuario.getEndereco());
        adicionadorDocumento.adicionarLink(usuario.getDocumentos());
        adicionadorEmail.adicionarLink(usuario.getEmails());
        adicionadorCredencial.adicionarLink(usuario.getCredenciais());
        adicionadorMercadoria.adicionarLink(usuario.getMercadorias());
    }
}
