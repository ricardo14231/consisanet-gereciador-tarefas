package com.consisanet.gerenciamento_tarefa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ResponseUnprocessableException extends RuntimeException {

    public ResponseUnprocessableException(String msg) {
        super(msg);
    }
}
