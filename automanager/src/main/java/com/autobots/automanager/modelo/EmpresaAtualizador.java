package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.entidades.Empresa;

public class EmpresaAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();

    public void atualizar(Empresa empresa, EmpresaDTO atualizacao) {
        if (!verificador.verificar(atualizacao.getRazaoSocial())) {
            empresa.setRazaoSocial(atualizacao.getRazaoSocial());
        }
        if (!verificador.verificar(atualizacao.getNomeFantasia())) {
            empresa.setNomeFantasia(atualizacao.getNomeFantasia());
        }
        enderecoAtualizador.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
    }
}
