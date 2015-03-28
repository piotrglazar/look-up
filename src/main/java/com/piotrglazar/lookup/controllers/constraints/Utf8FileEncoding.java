package com.piotrglazar.lookup.controllers.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Utf8FileEncodingValidator.class)
@Documented
public @interface Utf8FileEncoding {

    String message() default "Only UTF-8 is supported";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
