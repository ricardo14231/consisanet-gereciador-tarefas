package com.consisanet.gerenciamento_tarefa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResponseNotFoundException extends RuntimeException {

    public ResponseNotFoundException(String msg) {
        super(msg);
    }
}
