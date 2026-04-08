package com.SpringBootWebTutorial.Module2.WebTutorial.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
