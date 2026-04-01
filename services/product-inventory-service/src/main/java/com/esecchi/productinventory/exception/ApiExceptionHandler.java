package com.esecchi.productinventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        // Se verifica si el error generado es específicamente por un enum mal enviado en el request
        if (ex.getCause() instanceof InvalidFormatException invalidFormatEx) {
            if (invalidFormatEx.getTargetType().isEnum()) {
                String fieldName = invalidFormatEx.getPath().get(0).getPropertyName();
                String valueSent = invalidFormatEx.getValue().toString();
                String acceptedValues = Arrays.toString(invalidFormatEx.getTargetType().getEnumConstants());

                errorResponse.put("error", "Valor inválido para el campo: " + fieldName);
                errorResponse.put("message", String.format("El valor '%s' no es válido. Los valores permitidos son: %s", valueSent, acceptedValues));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        }
        // Error genérico de formato en el JSON recibido
        errorResponse.put("error", "Error en el formato del JSON");
        errorResponse.put("message", ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        HashMap<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
    }

}