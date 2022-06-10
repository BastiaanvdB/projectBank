package io.swagger.api;

import io.swagger.model.ResponseDTO.ExceptionResponseDTO;
import io.swagger.model.exception.AccountNotFoundException;
import io.swagger.model.exception.InvalidIbanException;
import io.swagger.model.exception.InvalidPincodeException;
import io.swagger.model.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO("Illegal input argument, follow input requirements.");
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO(ex.getMessage());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {AccountNotFoundException.class})
    protected ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO(ex.getMessage());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {InvalidIbanException.class})
    protected ResponseEntity<Object> handleInvalidIbanException(InvalidIbanException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO(ex.getMessage());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {InvalidPincodeException.class})
    protected ResponseEntity<Object> handleInvalidPincodeException(InvalidPincodeException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO(ex.getMessage());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}