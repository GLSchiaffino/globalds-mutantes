package org.globalds.mutantes.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDnaSequence {

    String message() default "La secuencia de ADN es inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
