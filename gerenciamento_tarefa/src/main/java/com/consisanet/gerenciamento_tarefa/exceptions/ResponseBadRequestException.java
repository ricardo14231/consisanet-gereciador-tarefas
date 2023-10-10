package com.consisanet.gerenciamento_tarefa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResponseBadRequestException extends RuntimeException {

    public ResponseBadRequestException(String msg) {
        super(msg);
    }
}
