package com.example.bankmanagement.BankApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.InsufficientResourcesException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AccountNotFoundException.class })
    public final ResponseEntity handleException(Exception ex, WebRequest request) {

        if(ex instanceof AccountNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;

            AccountNotFoundException anfe = (AccountNotFoundException) ex;
            return handleExceptionInternal(anfe, request, status);
        } else if(ex instanceof CustomerNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;

            CustomerNotFoundException cnfe = (CustomerNotFoundException) ex;
            return handleExceptionInternal(cnfe, request, status);
        } else if(ex instanceof InsufficientFundsException) {
            HttpStatus status = HttpStatus.PAYMENT_REQUIRED;

            InsufficientFundsException ife = (InsufficientFundsException) ex;
            return handleExceptionInternal(ife, request, status);
        } else if(ex instanceof ExceededDailyLimitException) {
            HttpStatus status = HttpStatus.CONFLICT;

            ExceededDailyLimitException edle = (ExceededDailyLimitException) ex;
            return handleExceptionInternal(edle, request, status);
        }

        return new ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
    }

    protected ResponseEntity<String> handleExceptionInternal(Exception ex, WebRequest webRequest, HttpStatus status) {
        ApiError thisError = new ApiError(ex.getMessage(), ex, webRequest);
        return new ResponseEntity<String>(thisError.toString(), status);
    }
}
