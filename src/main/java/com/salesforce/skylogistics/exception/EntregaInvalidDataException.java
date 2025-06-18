package com.salesforce.skylogistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class EntregaInvalidDataException extends RuntimeException {
    public EntregaInvalidDataException(String mensaje) {
        super(mensaje);
    }

    public static EntregaInvalidDataException badRequest(String mensaje) {
        return new EntregaInvalidDataException(mensaje);
    }
}
