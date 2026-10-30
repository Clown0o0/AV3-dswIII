package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.entidades.Servico;

public class ServicoAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Servico servico, ServicoDTO atualizacao) {
        if (!verificador.verificar(atualizacao.getNome())) {
            servico.setNome(atualizacao.getNome());
        }
        if (atualizacao.getValor() != null) {
            servico.setValor(atualizacao.getValor());
        }
        if (!verificador.verificar(atualizacao.getDescricao())) {
            servico.setDescricao(atualizacao.getDescricao());
        }
    }
}
