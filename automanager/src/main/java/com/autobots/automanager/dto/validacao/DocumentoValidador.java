package com.autobots.automanager.dto.validacao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.autobots.automanager.dto.DocumentoDTO;
import com.autobots.automanager.enumeracoes.TipoDocumento;

public class DocumentoValidador implements ConstraintValidator<DocumentoValido, DocumentoDTO> {

    @Override
    public boolean isValid(DocumentoDTO dto, ConstraintValidatorContext contexto) {
        if (dto == null || dto.getTipo() == null || dto.getNumero() == null || valido(dto.getTipo(), dto.getNumero())) {
            return true;
        }
        contexto.disableDefaultConstraintViolation();
        contexto.buildConstraintViolationWithTemplate("Número inválido para o tipo " + dto.getTipo())
                .addPropertyNode("numero")
                .addConstraintViolation();
        return false;
    }

    public static boolean valido(TipoDocumento tipo, String numero) {
        switch (tipo) {
        case CPF:
            return digitosVerificadores(numero.replaceAll("[.\\-]", ""), 11, new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2 });
        case CNPJ:
            return digitosVerificadores(numero.replaceAll("[./\\-]", ""), 14, new int[] { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 });
        case RG:
            return numero.matches("^[0-9A-Za-z.\\-]{5,14}$");
        case CNH:
            return numero.matches("^\\d{11}$");
        case PASSAPORTE:
            return numero.matches("^[A-Za-z]{2}\\d{6}$");
        default:
            return false;
        }
    }

    private static boolean digitosVerificadores(String numero, int tamanho, int[] pesos) {
        if (!numero.matches("\\d{" + tamanho + "}") || numero.chars().distinct().count() == 1) {
            return false;
        }
        int base = tamanho - 2;
        return digito(numero, base, pesos) == numero.charAt(base) - '0'
                && digito(numero, base + 1, pesos(pesos)) == numero.charAt(base + 1) - '0';
    }

    private static int digito(String numero, int quantidade, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += (numero.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private static int[] pesos(int[] pesos) {
        int[] proximos = new int[pesos.length + 1];
        proximos[0] = pesos[0] + 1;
        System.arraycopy(pesos, 0, proximos, 1, pesos.length);
        return proximos;
    }
}
