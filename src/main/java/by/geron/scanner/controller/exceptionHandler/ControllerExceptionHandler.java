package by.geron.scanner.controller.exceptionHandler;

import by.geron.scanner.dto.response.ResponseError;
import by.geron.scanner.exceptions.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    public ResponseError handleEntityExistsException(EntityExistsException e) {
        log.info(e.getMessage());
        return ResponseError.builder()
                .message("such entity already exists")
                .errorData(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    public ResponseError handleIOException(IOException e) {
        log.info(e.getMessage());
        return ResponseError.builder()
                .message("no such path")
                .errorData(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseError handleNoSuchElementException(NoSuchElementException e) {
        log.info(e.getMessage());
        return ResponseError.builder()
                .message("no such element")
                .errorData(e.getMessage())
                .build();
    }
}
