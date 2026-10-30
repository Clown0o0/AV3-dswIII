package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.entidades.Email;

public class EmailAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Email email, EmailDTO atualizacao) {
        if (atualizacao != null && !verificador.verificar(atualizacao.getEndereco())) {
            email.setEndereco(atualizacao.getEndereco());
        }
    }
}
