package com.consisanet.gerenciamento_tarefa.utils;

import com.consisanet.gerenciamento_tarefa.dtos.TarefaDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import com.consisanet.gerenciamento_tarefa.models.TarefaModel;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TarefaBuilder {

    public static TarefaModel createTarefaModel() {
        return TarefaModel.builder()
                .id(1L)
                .nomeTarefa("Tarefa 1")
                .dataInicio(LocalDate.of(2023, 10, 6))
                .dataFim(LocalDate.of(2023, 10, 10))
                .situacaoTarefa(SituacaoTarefaEnum.AGUARDANDO)
                .deletado(false)
                .updateAt(null)
                .createAt(LocalDateTime.of(2023, 10, 6, 16,0,0))
                .build();
    }

    public static TarefaDto createTarefaDto() {
        return TarefaDto.builder()
                .id(1L)
                .nomeTarefa("Tarefa 1")
                .dataInicio(LocalDate.of(2023, 10, 6))
                .dataFim(LocalDate.of(2023, 10, 10))
                .situacaoTarefa(SituacaoTarefaEnum.AGUARDANDO)
                .updateAt(null)
                .createAt(LocalDateTime.of(2023, 10, 6, 16,0,0))
                .build();
    }
}
