package com.example.instagram2.exception;

import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.InvalidFileException;
import com.example.instagram2.exception.myException.InvalidPasswordException;
import com.example.instagram2.exception.myException.NoAuthorityException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DuplicationException.class})
    public ResponseEntity<?> duplicatedArgsHandler(DuplicationException e) {
        log.error("DuplicationException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidPasswordException.class})
    public ResponseEntity<?> passwordHandler(InvalidPasswordException e){
        log.error("DuplicationException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> sqlErrorhandler(DataIntegrityViolationException e){
        log.error("DuplicationException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoAuthorityException.class})
    public ResponseEntity<?> noAuthorityHandler(NoAuthorityException e) {
        log.error("NoAuthorityHandler: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InvalidFileException.class})
    public ResponseEntity<?> illegalFileTypeHandler(InvalidFileException e) {
        log.error("IllegalFileType: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> runtimeHandler(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentConversionNotSupportedException.class})
    public ResponseEntity<?> validExceptionHandler(BindException e) {
        log.error("valid Error: {}", e.getMessage());
        e.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        ErrorRespDTO dto = ErrorRespDTO.builder()
                .exception(e.getClass().getSimpleName())
                .errors(errors)
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    private ErrorRespDTO errorToDTO(Exception e) {
        return ErrorRespDTO.builder()
                .exception(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }
}
