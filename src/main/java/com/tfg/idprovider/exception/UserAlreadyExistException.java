package com.tfg.idprovider.exception;

public class UserAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public UserAlreadyExistException(final String message) {
        super(message);
    }
}
