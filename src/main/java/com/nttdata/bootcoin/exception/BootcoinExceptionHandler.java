package com.nttdata.bootcoin.exception;

import com.nttdata.bootcoin.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BootcoinExceptionHandler {
    @ExceptionHandler(BootcoinNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCreditNotFoundException(BootcoinNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Bootcoin Not Found");
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidDataBootcoinException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCreditDataException(InvalidDataBootcoinException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Invalid Bootcoin Data");
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
