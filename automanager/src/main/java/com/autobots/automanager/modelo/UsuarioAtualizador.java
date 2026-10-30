package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.entidades.Usuario;

public class UsuarioAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();

    public void atualizar(Usuario usuario, UsuarioDTO atualizacao) {
        if (!verificador.verificar(atualizacao.getNome())) {
            usuario.setNome(atualizacao.getNome());
        }
        if (!verificador.verificar(atualizacao.getNomeSocial())) {
            usuario.setNomeSocial(atualizacao.getNomeSocial());
        }
        if (atualizacao.getPerfis() != null) {
            usuario.getPerfis().clear();
            usuario.getPerfis().addAll(atualizacao.getPerfis());
        }
        enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
    }
}
