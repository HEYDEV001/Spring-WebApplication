package com.SpringBootWebTutorial.Module2.WebTutorial.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE,ElementType.FIELD,ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EmployeeRoleValidator.class})
public @interface EmployeeRoleValidation {
    String message() default "{Role of Employee can Either ADMIN or USER}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    // Above is the Structure of the Annotation, but we have to build some logic here
    // For which we will create a validator.
    // validator is nothing just a simple class implementing the ConstraintValidator<> Interface which take Annotation name and the type of Field or input it will work on , As parameters.
}
