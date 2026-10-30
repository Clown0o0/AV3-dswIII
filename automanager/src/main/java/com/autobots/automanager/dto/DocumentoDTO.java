package com.autobots.automanager.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.DocumentoValido;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@DocumentoValido
public class DocumentoDTO {

    @NotNull(groups = Criacao.class, message = "O tipo do documento deve ser informado")
    private TipoDocumento tipo;

    @NotNull(groups = Criacao.class, message = "A data de emissão deve ser informada")
    @PastOrPresent(message = "A data de emissão não pode estar no futuro")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date dataEmissao;

    @NotBlank(groups = Criacao.class, message = "O número do documento deve ser informado")
    private String numero;

    @NotNull(groups = Vinculo.class, message = "O usuário dono do documento deve ser informado")
    private Long usuarioId;

    public Documento paraEntidade() {
        Documento documento = new Documento();
        documento.setTipo(tipo);
        documento.setDataEmissao(dataEmissao);
        documento.setNumero(numero);
        return documento;
    }
}
