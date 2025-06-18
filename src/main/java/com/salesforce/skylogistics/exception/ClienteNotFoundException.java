package com.salesforce.skylogistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String mensaje) {
        super(mensaje);
    }

    public static ClienteNotFoundException porId(String id) {
        return new ClienteNotFoundException("Cliente con ID: " + id + " no encontrado");
    }

    public static ClienteNotFoundException porEmail(String email) {
        return new ClienteNotFoundException("Cliente con email: " + email + " no encontrado");
    }

    public static ClienteNotFoundException general(String mensaje) {
        return new ClienteNotFoundException(mensaje);
    }
}
