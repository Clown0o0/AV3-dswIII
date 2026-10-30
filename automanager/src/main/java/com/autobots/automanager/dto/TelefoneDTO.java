package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.DonoUnico;
import com.autobots.automanager.dto.validacao.PossuiDono;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Telefone;

import lombok.Data;

@Data
@DonoUnico(groups = Vinculo.class)
public class TelefoneDTO implements PossuiDono {

    @NotBlank(groups = Criacao.class, message = "O DDD do telefone deve ser informado")
    @Pattern(regexp = "\\d{2}", message = "O DDD deve ter 2 dígitos")
    private String ddd;

    @NotBlank(groups = Criacao.class, message = "O número do telefone deve ser informado")
    @Pattern(regexp = "\\d{4,5}-?\\d{4}", message = "O número do telefone deve ter 8 ou 9 dígitos")
    private String numero;

    private Long usuarioId;

    private Long empresaId;

    public Telefone paraEntidade() {
        Telefone telefone = new Telefone();
        telefone.setDdd(ddd);
        telefone.setNumero(numero);
        return telefone;
    }
}
