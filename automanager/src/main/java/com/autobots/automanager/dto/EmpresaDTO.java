package com.autobots.automanager.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;

import lombok.Data;

@Data
public class EmpresaDTO {

    @NotBlank(groups = Criacao.class, message = "A razão social deve ser informada")
    @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
    private String razaoSocial;

    @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres")
    private String nomeFantasia;

    @Valid
    private List<TelefoneDTO> telefones;

    @Valid
    private EnderecoDTO endereco;
}
