package io.swagger.api;

import io.swagger.model.ResponseDTO.ExceptionResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO("Illegal input argument, follow input requirements.");
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

}
