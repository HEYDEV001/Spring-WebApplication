package com.SpringBootWebTutorial.Module2.WebTutorial.advices;

import com.SpringBootWebTutorial.Module2.WebTutorial.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice// This take care of all the Controllers.Means we can use this exception Handler in all the Controllers
public class CustomGlobalException {
    private final View error;

    public CustomGlobalException(View error) {
        this.error = error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> HandleResourceNotFound(ResourceNotFoundException exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return  new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);// The message given here will be shown.
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found");
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> HandleInternalServerException(Exception exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return  new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> HandleMethodArgumentNotValid(MethodArgumentNotValidException exception){
        List<String> errors = exception.
                getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation Failed")
                .subErrors(errors)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}