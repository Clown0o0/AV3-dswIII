package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.CredencialCodigoBarraDTO;
import com.autobots.automanager.dto.CredencialDTO;
import com.autobots.automanager.dto.CredencialUsuarioSenhaDTO;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;

public class CredencialAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Credencial credencial, CredencialDTO atualizacao) {
        if (atualizacao.getInativo() != null) {
            credencial.setInativo(atualizacao.getInativo());
        }
        if (credencial instanceof CredencialUsuarioSenha && atualizacao instanceof CredencialUsuarioSenhaDTO) {
            CredencialUsuarioSenha alvo = (CredencialUsuarioSenha) credencial;
            CredencialUsuarioSenhaDTO dados = (CredencialUsuarioSenhaDTO) atualizacao;
            if (!verificador.verificar(dados.getNomeUsuario())) {
                alvo.setNomeUsuario(dados.getNomeUsuario());
            }
            if (!verificador.verificar(dados.getSenha())) {
                alvo.setSenha(dados.getSenha());
            }
        }
        if (credencial instanceof CredencialCodigoBarra && atualizacao instanceof CredencialCodigoBarraDTO) {
            CredencialCodigoBarraDTO dados = (CredencialCodigoBarraDTO) atualizacao;
            if (!verificador.verificar(dados.getCodigo())) {
                ((CredencialCodigoBarra) credencial).setCodigo(dados.getCodigo());
            }
        }
    }
}
