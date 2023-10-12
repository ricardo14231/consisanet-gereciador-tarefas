package com.consisanet.gerenciamento_tarefa.services;

import com.consisanet.gerenciamento_tarefa.Mappers.TarefaMapper;
import com.consisanet.gerenciamento_tarefa.Mappers.UsuarioMapper;
import com.consisanet.gerenciamento_tarefa.dtos.MessageResponseDto;
import com.consisanet.gerenciamento_tarefa.dtos.TarefaDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import com.consisanet.gerenciamento_tarefa.exceptions.ResponseNotFoundException;
import com.consisanet.gerenciamento_tarefa.exceptions.ResponseUnprocessableException;
import com.consisanet.gerenciamento_tarefa.models.TarefaModel;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;
import com.consisanet.gerenciamento_tarefa.repositorys.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired UsuarioService usuarioService;

    public List<TarefaDto> listAllTarefa() {
        return tarefaRepository.findAllByDeletadoFalse().stream()
                .map(TarefaMapper::toDto)
                .collect(Collectors.toList());
    }

    public TarefaDto findByIdTarefa(Long id) {
        return TarefaMapper.toDto(verifyIfExistsTarefa(id));
    }

    public TarefaDto createTarefa(TarefaDto tarefaDto) {
        TarefaModel tarefaToSave = TarefaMapper.toModel(tarefaDto);

        verifyDuplicateNameTarefa(tarefaToSave);

        if(tarefaToSave.getUsuarioResponsavel() != null) {
            UsuarioDto usuarioToSave = usuarioService
                    .findByIdUsuario(tarefaToSave.getUsuarioResponsavel().getId());

            tarefaToSave.setUsuarioResponsavel(UsuarioMapper.toModel(usuarioToSave));
        }

        if(tarefaDto.getTarefaPrincipal() != null) {
            TarefaModel principalTarefa = verifyIfExistsTarefa(tarefaDto.getTarefaPrincipal().getId());
            tarefaToSave.setTarefaPrincipal(principalTarefa);
        }

        return TarefaMapper.toDto(tarefaRepository.save(tarefaToSave));
    }

    public TarefaDto updateTarefa(TarefaDto tarefaDto) {

        verifyIfExistsTarefa(tarefaDto.getId());

        TarefaModel tarefaToSave = TarefaMapper.toModel(tarefaDto);

        verifyDuplicateNameTarefa(tarefaToSave);

        if(tarefaDto.getUsuarioResponsavel() != null) {
            UsuarioModel usuario =
                    UsuarioMapper.toModel(
                            usuarioService.findByIdUsuario(tarefaDto.getUsuarioResponsavel().getId())
                    );

            tarefaToSave.setUsuarioResponsavel(usuario);
        }
        if(tarefaDto.getTarefaPrincipal() != null) {
            TarefaModel principalTarefa = verifyIfExistsTarefa(tarefaDto.getTarefaPrincipal().getId());
            tarefaToSave.setTarefaPrincipal(principalTarefa);
        }

        tarefaToSave.setUpdateAt(LocalDateTime.now());

        return TarefaMapper.toDto(tarefaRepository.save(tarefaToSave));
    }

    @Transactional
    public MessageResponseDto deleteTarefa(Long id) {
        TarefaModel tarefaToDelete = verifyIfExistsTarefa(id);

        if(tarefaToDelete.getUsuarioResponsavel() != null) {
            throw new ResponseUnprocessableException("Não é possível deletar uma tarefa com um usuário associado a ela!");
        }

        List<TarefaModel> listTarefa = tarefaRepository.findByTarefaPrincipal(tarefaToDelete);

        listTarefa.forEach(t -> {
            t.setDeletado(true);
            t.setUpdateAt(LocalDateTime.now());
            if(t.getTarefaPrincipal() != null) {
                TarefaModel subTarefa = tarefaRepository.findByTarefaPrincipalId(t.getId());
                subTarefa.setDeletado(true);
                subTarefa.setUpdateAt(LocalDateTime.now());
            }
            t.getTarefaPrincipal().setDeletado(true);
            t.getTarefaPrincipal().setUpdateAt(LocalDateTime.now());
        });

        tarefaRepository.saveAll(listTarefa);

        tarefaToDelete.setDeletado(true);
        tarefaToDelete.setUpdateAt(LocalDateTime.now());

        tarefaRepository.save(tarefaToDelete);

        return new MessageResponseDto("Recurso com Id: " + id + " deletado");
    }

    private TarefaModel verifyIfExistsTarefa(Long id) {
        return tarefaRepository.findByIdAndDeletadoFalse(id)
                .orElseThrow(() -> new ResponseNotFoundException("Tarefa com Id: " + id + " não encontrada"));
    }

    public List<TarefaDto> filterDate(LocalDate initialDate, LocalDate finalDate) {

        if(initialDate == null || finalDate == null) {
            throw new IllegalArgumentException("O campo data inicial ou data final está vazio");
        }

        LocalDateTime init = LocalDateTime.of(initialDate, LocalTime.of(0,0,0));
        LocalDateTime last = LocalDateTime.of(finalDate, LocalTime.of(23,59,59));

        return tarefaRepository.findByDeletadoFalseAndCreateAtBetween(init, last).stream()
                .map(TarefaMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TarefaDto> listAllTarefaAtrasada() {

        LocalDate date = LocalDate.now();
        LocalDateTime finalCurrentDate = LocalDateTime.of(
                date.getYear(),
                date.getMonth(),
                date.getDayOfMonth(),
            23,59,59);

        return tarefaRepository.findAllTarefaAtrasada(finalCurrentDate)
                .stream()
                .map(TarefaMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TarefaDto> listTarefaByUsuario(Long usuarioId) {

        return tarefaRepository.listTarefaByUsuario(
                        usuarioId).stream()
                .map(TarefaMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TarefaDto> listTarefaByUsuarioAndStatus(Long usuarioId, SituacaoTarefaEnum statusTarefa) {

        return tarefaRepository.listTarefaByUsuarioAndStatus(
                usuarioId, statusTarefa.situacao).stream()
                .map(TarefaMapper::toDto)
                .collect(Collectors.toList());

    }

    private void verifyDuplicateNameTarefa(TarefaModel tarefaModel) {
        List<TarefaModel> listTarefa = tarefaRepository.findAllByDeletadoFalse();

        for (TarefaModel tarefa : listTarefa) {
            if (tarefa.getNomeTarefa().equals(tarefaModel.getNomeTarefa()) &&
            tarefa.getId() != tarefaModel.getId()) {
                throw new ResponseUnprocessableException("Já existe uma tarefa cadastra com esse nome!");
            }
        }
    }
}
