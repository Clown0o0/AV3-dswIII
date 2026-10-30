package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.entidades.Documento;

public class DocumentoAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Documento documento, DocumentoDTO atualizacao) {
        if (atualizacao == null) {
            return;
        }
        if (atualizacao.getTipo() != null) {
            documento.setTipo(atualizacao.getTipo());
        }
        if (atualizacao.getDataEmissao() != null) {
            documento.setDataEmissao(atualizacao.getDataEmissao());
        }
        if (!verificador.verificar(atualizacao.getNumero())) {
            documento.setNumero(atualizacao.getNumero());
        }
    }
}
