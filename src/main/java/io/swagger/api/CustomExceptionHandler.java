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

    @ExceptionHandler(value = { NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
        ExceptionResponseDTO dto = new ExceptionResponseDTO("Ey joh dus null man!");
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

}
