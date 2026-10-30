package com.autobots.automanager.dto.validacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DocumentoValidador.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentoValido {

    String message() default "Número inválido para o tipo de documento informado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
