package com.consisanet.gerenciamento_tarefa.enums;

public enum SituacaoTarefaEnum {
    AGUARDANDO("AGUARDANDO"),
    INICIADA("INICIADA"),
    ENCERRADA("ENCERRADA");

    public String situacao;

    SituacaoTarefaEnum(String status) {
        situacao = status;
    }
}
