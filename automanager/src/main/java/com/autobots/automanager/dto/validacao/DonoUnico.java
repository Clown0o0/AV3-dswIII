package com.autobots.automanager.dto.validacao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DonoUnicoValidador.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DonoUnico {

    String message() default "Informe exatamente um dono: usuarioId ou empresaId";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
