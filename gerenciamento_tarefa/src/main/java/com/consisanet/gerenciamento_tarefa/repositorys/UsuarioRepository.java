package com.consisanet.gerenciamento_tarefa.repositorys;

import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    public List<UsuarioModel> findAllByDeletadoFalse();
    Optional<UsuarioModel> findByIdAndDeletadoFalse(Long id);

}
