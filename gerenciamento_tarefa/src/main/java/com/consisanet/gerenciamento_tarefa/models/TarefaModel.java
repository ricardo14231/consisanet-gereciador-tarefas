package com.consisanet.gerenciamento_tarefa.models;

import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "tarefa")
public class TarefaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_tarefa", nullable = false, length = 150)
    private String nomeTarefa;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuarioResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_tarefa", nullable = false)
    private SituacaoTarefaEnum situacaoTarefa;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tarefa_principal_id")
    private TarefaModel tarefaPrincipal;

    private boolean deletado;

    private LocalDateTime updateAt;

    private LocalDateTime createAt = LocalDateTime.now();
}
