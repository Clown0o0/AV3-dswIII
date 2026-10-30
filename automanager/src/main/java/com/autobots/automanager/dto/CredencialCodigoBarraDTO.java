package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.entidades.CredencialCodigoBarra;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CredencialCodigoBarraDTO extends CredencialDTO {

    @NotBlank(groups = Criacao.class, message = "O código de barras deve ser informado")
    @Pattern(regexp = "\\d{8,20}", message = "O código de barras deve ter de 8 a 20 dígitos")
    private String codigo;

    @Override
    public CredencialCodigoBarra paraEntidade() {
        CredencialCodigoBarra credencial = new CredencialCodigoBarra();
        credencial.setCodigo(codigo);
        return credencial;
    }
}
