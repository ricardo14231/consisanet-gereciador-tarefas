package com.consisanet.gerenciamento_tarefa.utils;

import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;

import java.time.LocalDateTime;

public class UsuarioBuilder {
    public static UsuarioModel createUsuarioModel() {
        return UsuarioModel.builder()
                .id(1L)
                .nome("User 1")
                .deletado(false)
                .updateAt(null)
                .createAt(LocalDateTime.of(2023, 10, 1, 8, 0, 0))
                .build();
    }

    public static UsuarioDto createUsuarioDto() {
        return UsuarioDto.builder()
                .id(1L)
                .nome("User 1")
                .updateAt(null)
                .createAt(LocalDateTime.of(2023, 10, 1, 8, 0, 0))
                .build();
    }
}
