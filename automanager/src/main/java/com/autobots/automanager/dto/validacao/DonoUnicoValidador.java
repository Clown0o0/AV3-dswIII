package com.autobots.automanager.dto.validacao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DonoUnicoValidador implements ConstraintValidator<DonoUnico, PossuiDono> {

    @Override
    public boolean isValid(PossuiDono dto, ConstraintValidatorContext contexto) {
        if (dto == null || (dto.getUsuarioId() == null) != (dto.getEmpresaId() == null)) {
            return true;
        }
        contexto.disableDefaultConstraintViolation();
        contexto.buildConstraintViolationWithTemplate(contexto.getDefaultConstraintMessageTemplate())
                .addPropertyNode("usuarioId")
                .addConstraintViolation();
        return false;
    }
}
