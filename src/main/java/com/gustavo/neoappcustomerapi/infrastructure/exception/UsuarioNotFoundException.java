package com.gustavo.neoappcustomerapi.infrastructure.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id) {
        super("Cliente com ID " + id + " não encontrado");
    }
}

