package com.webapp.webapp.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.RuntimeException;

@Component
public class MyExceptions extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMessage;

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public MyExceptions() {

    }

    public MyExceptions(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Component
    public static class ControllerException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private String errorCode;
        private String errorMessage;

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public ControllerException() {

        }

        public ControllerException(String errorCode, String errorMessage) {
            super();
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    @Component
    public static class UserException extends RuntimeException {

        private static final long serialVersionUID = 1L;

    }

    @ControllerAdvice
    public static class UserExceptionController {
        @ExceptionHandler(value = UserException.class)
        public ResponseEntity<?> exception(UserException exception) {
            return new ResponseEntity<>("Username already in use.", HttpStatus.BAD_REQUEST);
        }
    }
}
