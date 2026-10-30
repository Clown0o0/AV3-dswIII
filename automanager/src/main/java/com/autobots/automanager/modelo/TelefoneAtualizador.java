package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Telefone;

public class TelefoneAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Telefone telefone, TelefoneDTO atualizacao) {
        if (atualizacao == null) {
            return;
        }
        if (!verificador.verificar(atualizacao.getDdd())) {
            telefone.setDdd(atualizacao.getDdd());
        }
        if (!verificador.verificar(atualizacao.getNumero())) {
            telefone.setNumero(atualizacao.getNumero());
        }
    }
}
