package com.esecchi.userauth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("No se ha podido encontrar el usuario con el id: " + String.valueOf(userId));
    }
}
