package com.autobots.automanager.dto.validacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PlacaValidador.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Placa {

    String message() default "A placa deve seguir o padrão ABC-1234 ou Mercosul ABC1D23";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
