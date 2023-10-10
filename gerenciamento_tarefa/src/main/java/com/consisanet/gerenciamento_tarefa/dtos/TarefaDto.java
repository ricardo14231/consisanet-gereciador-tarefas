package com.consisanet.gerenciamento_tarefa.dtos;

import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class TarefaDto {

    private Long id;

    @NotBlank
    @Size(max = 150, message = "O campo nomeTarefa não deve ser maior que {max}")
    private String nomeTarefa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    private UsuarioDto usuarioResponsavel;

    private TarefaDto tarefaPrincipal;

    @NotNull
    private SituacaoTarefaEnum situacaoTarefa;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
}
