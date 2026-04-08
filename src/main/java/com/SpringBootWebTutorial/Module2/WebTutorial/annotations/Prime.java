package com.SpringBootWebTutorial.Module2.WebTutorial.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsPrime.class})
public @interface Prime {
    String message() default "{Enter the Prime Number}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    // Above is the Structure of the Annotation, but we have to build some logic here
}
