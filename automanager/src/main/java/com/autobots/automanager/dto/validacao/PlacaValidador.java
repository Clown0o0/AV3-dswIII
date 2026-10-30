package com.autobots.automanager.dto.validacao;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PlacaValidador implements ConstraintValidator<Placa, String> {

    private static final Pattern PADRAO = Pattern.compile("^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$");

    @Override
    public boolean isValid(String placa, ConstraintValidatorContext contexto) {
        return placa == null || PADRAO.matcher(placa.toUpperCase()).matches();
    }
}
