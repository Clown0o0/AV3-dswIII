package com.autobots.automanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.dto.validacao.DonoUnico;
import com.autobots.automanager.dto.validacao.PossuiDono;
import com.autobots.automanager.dto.validacao.Vinculo;
import com.autobots.automanager.entidades.Endereco;

import lombok.Data;

@Data
@DonoUnico(groups = Vinculo.class)
public class EnderecoDTO implements PossuiDono {

    @NotBlank(groups = Criacao.class, message = "O estado deve ser informado")
    @Size(max = 50, message = "O estado deve ter no máximo 50 caracteres")
    private String estado;

    @NotBlank(groups = Criacao.class, message = "A cidade deve ser informada")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @NotBlank(groups = Criacao.class, message = "O bairro deve ser informado")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @NotBlank(groups = Criacao.class, message = "A rua deve ser informada")
    @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres")
    private String rua;

    @NotBlank(groups = Criacao.class, message = "O número do endereço deve ser informado")
    @Size(max = 10, message = "O número do endereço deve ter no máximo 10 caracteres")
    private String numero;

    @NotBlank(groups = Criacao.class, message = "O código postal deve ser informado")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "O código postal deve ter 8 dígitos")
    private String codigoPostal;

    @Size(max = 255, message = "As informações adicionais devem ter no máximo 255 caracteres")
    private String informacoesAdicionais;

    private Long usuarioId;

    private Long empresaId;

    public Endereco paraEntidade() {
        Endereco endereco = new Endereco();
        endereco.setEstado(estado);
        endereco.setCidade(cidade);
        endereco.setBairro(bairro);
        endereco.setRua(rua);
        endereco.setNumero(numero);
        endereco.setCodigoPostal(codigoPostal);
        endereco.setInformacoesAdicionais(informacoesAdicionais);
        return endereco;
    }
}
