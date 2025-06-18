package com.salesforce.skylogistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntregaNotFoundException extends RuntimeException {

    public EntregaNotFoundException(String mensaje) {
        super(mensaje);
    }

    public static EntregaNotFoundException porId(String id) {
        return new EntregaNotFoundException("Cliente con ID: " + id + " no encontrado");
    }

    public static EntregaNotFoundException porEmail(String email) {
        return new EntregaNotFoundException("Cliente con email: " + email + " no encontrado");
    }

    public static EntregaNotFoundException general(String mensaje) {
        return new EntregaNotFoundException(mensaje);
    }
}
