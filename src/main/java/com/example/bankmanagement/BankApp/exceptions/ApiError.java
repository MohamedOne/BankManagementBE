package com.example.bankmanagement.BankApp.exceptions;

import lombok.Data;
import org.springframework.web.context.request.WebRequest;

@Data
public class ApiError {

    private String exceptionError;
    private Exception exception;
    private WebRequest webRequest;

    public ApiError(String exceptionError, Exception ex, WebRequest webRequest) {
        this.exceptionError = exceptionError;
        this.exception = ex;
        this.webRequest = webRequest;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "exceptionError='" + exceptionError + '\'' +
                ", exception=" + exception +
                ", webRequest=" + webRequest +
                '}';
    }
}
