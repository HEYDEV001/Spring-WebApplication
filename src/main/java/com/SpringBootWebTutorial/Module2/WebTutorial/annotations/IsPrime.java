package com.SpringBootWebTutorial.Module2.WebTutorial.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsPrime implements ConstraintValidator<Prime, Integer> {
    @Override
    public boolean isValid(Integer num, ConstraintValidatorContext constraintValidatorContext) {
        if (num <= 1) return false; // 0, 1, and negatives are not prime
        if (num == 2) return true;  // 2 is prime
        if (num % 2 == 0) return false; // even numbers greater than 2 are not prime

        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) {
                return false; // found a divisor, not prime
            }
        }
        return true; // no divisors found, prime
    }
}
