package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;

public class MercadoriaAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Mercadoria mercadoria, MercadoriaDTO atualizacao) {
        if (!verificador.verificar(atualizacao.getNome())) {
            mercadoria.setNome(atualizacao.getNome());
        }
        if (atualizacao.getValidade() != null) {
            mercadoria.setValidade(atualizacao.getValidade());
        }
        if (atualizacao.getFabricao() != null) {
            mercadoria.setFabricao(atualizacao.getFabricao());
        }
        if (atualizacao.getQuantidade() != null) {
            mercadoria.setQuantidade(atualizacao.getQuantidade());
        }
        if (atualizacao.getValor() != null) {
            mercadoria.setValor(atualizacao.getValor());
        }
        if (!verificador.verificar(atualizacao.getDescricao())) {
            mercadoria.setDescricao(atualizacao.getDescricao());
        }
    }
}
