package com.autobots.automanager.dto;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Atualizacao;
import com.autobots.automanager.dto.validacao.Criacao;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(groups = Criacao.class, message = "O nome do usuário deve ser informado")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 100, message = "O nome social deve ter no máximo 100 caracteres")
    private String nomeSocial;

    @NotEmpty(groups = Criacao.class, message = "Informe ao menos um perfil: CLIENTE, FUNCIONARIO ou FORNECEDOR")
    @Size(min = 1, groups = Atualizacao.class, message = "A lista de perfis não pode ficar vazia")
    private Set<@NotNull(message = "Perfil inválido") PerfilUsuario> perfis;

    private Long empresaId;

    @Valid
    private List<TelefoneDTO> telefones;

    @Valid
    private EnderecoDTO endereco;

    @Valid
    private List<DocumentoDTO> documentos;

    @Valid
    private List<EmailDTO> emails;

    @Valid
    private List<CredencialDTO> credenciais;
}
