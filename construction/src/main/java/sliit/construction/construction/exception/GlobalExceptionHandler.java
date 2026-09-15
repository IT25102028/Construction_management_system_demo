package sliit.construction.construction.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    record ErrorResponse(LocalDateTime timestamp,int status,String error,String message,Map<String,String> validationErrors) {}

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(LocalDateTime.now(),404,"Not Found",ex.getMessage(),Map.of()));
    }
    @ExceptionHandler(DuplicateResourceException.class)
    ResponseEntity<ErrorResponse> duplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(LocalDateTime.now(),409,"Conflict",ex.getMessage(),Map.of()));
    }
    @ExceptionHandler(UnauthorizedActionException.class)
    ResponseEntity<ErrorResponse> unauthorized(UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(LocalDateTime.now(),403,"Forbidden",ex.getMessage(),Map.of()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        Map<String,String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(e->e.getField(), e->e.getDefaultMessage()==null?"Invalid value":e.getDefaultMessage(), (a,b)->a, LinkedHashMap::new));
        return ResponseEntity.badRequest().body(new ErrorResponse(LocalDateTime.now(),400,"Validation Failed","Check the request fields",errors));
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(LocalDateTime.now(),500,"Internal Server Error",ex.getMessage()==null?"Unexpected error":ex.getMessage(),Map.of()));
    }
}
