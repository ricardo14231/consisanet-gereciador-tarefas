package com.consisanet.gerenciamento_tarefa.repositorys;

import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import com.consisanet.gerenciamento_tarefa.models.TarefaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<TarefaModel, Long> {

    Optional<TarefaModel> findByIdAndDeletadoFalse(Long id);

    List<TarefaModel> findByTarefaPrincipal(TarefaModel tarefaModel);

    List<TarefaModel> findAllByDeletadoFalse();

    List<TarefaModel> findByDeletadoFalseAndCreateAtBetween(LocalDateTime initialDate, LocalDateTime finalDate);

    List<TarefaModel> findByTarefaPrincipalId(Long id);

    @Query(value =
            "SELECT * FROM tarefa t " +
            "WHERE t.deletado = false " +
            "AND t.situacao_tarefa != 'ENCERRADA' " +
            "AND t.data_fim < ?1",
            nativeQuery = true
    )
    List<TarefaModel> findAllTarefaAtrasada(LocalDateTime finalCurrentDate);

    @Query(value =
            "SELECT * FROM tarefa t " +
                    "WHERE t.deletado = false " +
                    "AND t.situacaoTarefa != 'ENCERRADO'" +
                    "AND usuario_id = ?1 ",
            nativeQuery = true)
    List<TarefaModel> listTarefaByUsuario(Long usuarioId);

    @Query(value =
            "SELECT * FROM tarefa t " +
            "WHERE t.deletado = false " +
            "AND usuario_id = ?1 " +
            "AND situacao_tarefa = ?2",
            nativeQuery = true)
    List<TarefaModel> listTarefaByUsuarioAndStatus(Long usuarioId, String statusTarefa);
}
