package com.autobots.automanager.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroRespostaDTO {

    private String mensagem;

    private int status;

    private Map<String, String> campos;

    public ErroRespostaDTO(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
    }
}
