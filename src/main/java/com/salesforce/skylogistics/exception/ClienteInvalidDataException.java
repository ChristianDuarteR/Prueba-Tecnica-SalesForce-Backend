package com.salesforce.skylogistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class ClienteInvalidDataException extends RuntimeException {
    public ClienteInvalidDataException(String mensaje) {
        super(mensaje);
    }

    public static ClienteInvalidDataException badRequest(String mensaje) {
        return new ClienteInvalidDataException(mensaje);
    }
}
