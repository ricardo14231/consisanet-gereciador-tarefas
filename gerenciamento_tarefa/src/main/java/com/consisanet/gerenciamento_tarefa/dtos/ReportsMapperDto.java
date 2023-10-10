package com.consisanet.gerenciamento_tarefa.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportsMapperDto {

    private Long id;
    private String nomeTarefa;
    private String dataFim;
    private String usuarioResponsavel;
    private String tarefaPrincipal;
    private String situacaoTarefa;
    private String createAt;
}
