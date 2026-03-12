package com.esecchi.userauth.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {

    /**
     * Excepción lanzada cuando se intenta registrar un usuario con un email
     * que ya se encuentra asociado a una cuenta existente en el sistema.
     * @param email email que ya se encuentra registrado en el sistema
     */
    public EmailAlreadyRegisteredException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }

}