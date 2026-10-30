package com.autobots.automanager.dto;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.autobots.automanager.dto.validacao.Criacao;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MercadoriaDTO {

    @NotBlank(groups = Criacao.class, message = "O nome da mercadoria deve ser informado")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(groups = Criacao.class, message = "A validade deve ser informada")
    @Future(message = "A validade deve ser uma data futura")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date validade;

    @NotNull(groups = Criacao.class, message = "A data de fabricação deve ser informada")
    @PastOrPresent(message = "A data de fabricação não pode estar no futuro")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private Date fabricao;

    @NotNull(groups = Criacao.class, message = "A quantidade deve ser informada")
    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private Long quantidade;

    @NotNull(groups = Criacao.class, message = "O valor deve ser informado")
    @Positive(message = "O valor deve ser maior que zero")
    private Double valor;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @NotNull(groups = Criacao.class, message = "A empresa da mercadoria deve ser informada")
    private Long empresaId;

    private Long fornecedorId;
}
