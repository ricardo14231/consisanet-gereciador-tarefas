package com.consisanet.gerenciamento_tarefa.services;

import com.consisanet.gerenciamento_tarefa.Mappers.TarefaMapper;
import com.consisanet.gerenciamento_tarefa.dtos.MessageResponseDto;
import com.consisanet.gerenciamento_tarefa.dtos.TarefaDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import com.consisanet.gerenciamento_tarefa.exceptions.ResponseNotFoundException;
import com.consisanet.gerenciamento_tarefa.exceptions.ResponseUnprocessableException;
import com.consisanet.gerenciamento_tarefa.models.TarefaModel;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;
import com.consisanet.gerenciamento_tarefa.repositorys.TarefaRepository;
import com.consisanet.gerenciamento_tarefa.utils.TarefaBuilder;
import com.consisanet.gerenciamento_tarefa.utils.UsuarioBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private TarefaService tarefaService;


    @Test
    @DisplayName("Deve retornar uma lista de tarefas")
    void whenResquetListTarefaItShouldReturnListTarefa() {
        List<TarefaModel> listTarefaMock = Collections.singletonList(TarefaBuilder.createTarefaModel());

        when(tarefaRepository.findAllByDeletadoFalse()).thenReturn(listTarefaMock);

        List<TarefaDto> responseList = tarefaService.listAllTarefa();

        List<TarefaDto> listExpected = listTarefaMock.stream()
                .map(TarefaMapper::toDto)
                .collect(Collectors.toList());

        assertEquals(listExpected, responseList);
        verify(tarefaRepository).findAllByDeletadoFalse();
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas vazia")
    void whenRequestListTarefaThenItShouldReturnListEmpty() {
        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.emptyList());

        assertEquals(Collections.EMPTY_LIST, tarefaService.listAllTarefa());
    }

    @Test
    @DisplayName("Deve retornar uma tarefa por Id")
    void whenResquetTarefaByIdItShouldReturnTarefa() {
        TarefaModel tarefaMock = TarefaBuilder.createTarefaModel();

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaMock.getId()))
                .thenReturn(Optional.of(tarefaMock));

        TarefaDto response = tarefaService.findByIdTarefa(1L);

        TarefaDto tarefaExpected = TarefaMapper.toDto(tarefaMock);

        assertEquals(tarefaExpected, response);
        verify(tarefaRepository).findByIdAndDeletadoFalse(tarefaMock.getId());
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve criar uma tarefa sem usuário principal e sem sub tarefa")
    void whenRequestCreateTarefaWithoutUserAndTaskThenItShouldReturnCreated() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.singletonList(tarefaModelMock));

        TarefaDto tarefaCurrent = tarefaService.createTarefa(tarefaDtoMock);

        assertEquals(tarefaDtoMock, tarefaCurrent);
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve criar uma tarefa com um usuário responsável e sem sub tarefa")
    void whenRequestCreateTarefaWithUserThenItShouldReturnCreated() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        tarefaModelMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioModel());

        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaDtoMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioDto());

        when(usuarioService.findByIdUsuario(tarefaDtoMock.getUsuarioResponsavel().getId()))
                .thenReturn(UsuarioBuilder.createUsuarioDto());

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.singletonList(tarefaModelMock));

        TarefaDto tarefaCurrent = tarefaService.createTarefa(tarefaDtoMock);

        assertEquals(tarefaDtoMock, tarefaCurrent);
        verify(usuarioService).findByIdUsuario(UsuarioBuilder.createUsuarioDto().getId());
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve criar uma tarefa com um usuário e uma sub tarefa")
    void whenRequestCreateTarefaWithoutTaskThenItShouldReturnCreated() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        tarefaModelMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioModel());
        tarefaModelMock.setTarefaPrincipal(TarefaBuilder.createTarefaModel());

        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaDtoMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioDto());
        tarefaDtoMock.setTarefaPrincipal(TarefaBuilder.createTarefaDto());

        when(usuarioService.findByIdUsuario(tarefaDtoMock.getUsuarioResponsavel().getId()))
                .thenReturn(UsuarioBuilder.createUsuarioDto());

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaDtoMock.getId()))
                .thenReturn(Optional.of(TarefaBuilder.createTarefaModel()));

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.singletonList(tarefaModelMock));

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        TarefaDto tarefaCurrent = tarefaService.createTarefa(tarefaDtoMock);

        assertEquals(tarefaDtoMock, tarefaCurrent);
        verify(usuarioService).findByIdUsuario(UsuarioBuilder.createUsuarioDto().getId());
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Não deve criar duas tarefas com o mesmo nome")
    void whenRequestCreateTarefaThenItShouldReturnUnProcessable() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaModelMock.setId(2L);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.singletonList(tarefaModelMock));

        Assertions.assertThrows(ResponseUnprocessableException.class,
                () -> tarefaService.createTarefa(tarefaDtoMock));

        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa sem um usuário e sem uma sub tarefa")
    void whenRequestUpdateTarefaWithoutUserAndTaskThenItShouldReturnUpdated() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        tarefaModelMock.setNomeTarefa("User Updated");

        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaDtoMock.setNomeTarefa("User Updated");

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaDtoMock.getId()))
                .thenReturn(Optional.of(TarefaBuilder.createTarefaModel()));

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.EMPTY_LIST);

        TarefaDto tarefaCurrent = tarefaService.updateTarefa(tarefaDtoMock);

        assertEquals(tarefaDtoMock, tarefaCurrent);
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa com um usuário e sem uma sub tarefa")
    void whenRequestUpdateTarefaWithoutTaskThenItShouldReturnUpdated() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        UsuarioModel userUpdate = UsuarioBuilder.createUsuarioModel();
        userUpdate.setId(2L);
        tarefaModelMock.setUsuarioResponsavel(userUpdate);

        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        UsuarioDto userDtoUpdate = UsuarioBuilder.createUsuarioDto();
        userDtoUpdate.setId(2L);
        tarefaDtoMock.setUsuarioResponsavel(userDtoUpdate);

        when(usuarioService.findByIdUsuario(tarefaDtoMock.getUsuarioResponsavel().getId()))
                .thenReturn(UsuarioBuilder.createUsuarioDto());

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaDtoMock.getId()))
                .thenReturn(Optional.of(TarefaBuilder.createTarefaModel()));

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.EMPTY_LIST);

        TarefaDto tarefaCurrent = tarefaService.updateTarefa(tarefaDtoMock);

        assertEquals(tarefaDtoMock, tarefaCurrent);
        verify(usuarioService).findByIdUsuario(tarefaDtoMock.getUsuarioResponsavel().getId());
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa sem um usuário e com uma sub tarefa")
    void whenRequestUpdateTarefaWithoutUserThenItShouldReturnUpdated() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        TarefaModel tarefaUpdate = TarefaBuilder.createTarefaModel();

        tarefaUpdate.setId(2L);
        tarefaModelMock.setTarefaPrincipal(tarefaUpdate);


        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        TarefaDto tarefaDtoUpdate = TarefaBuilder.createTarefaDto();

        tarefaDtoUpdate.setId(2L);
        tarefaDtoMock.setTarefaPrincipal(tarefaDtoUpdate);

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaDtoMock.getId()))
                .thenReturn(Optional.of(TarefaBuilder.createTarefaModel()));

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaUpdate.getId()))
                .thenReturn(Optional.of(tarefaUpdate));

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.EMPTY_LIST);

        TarefaDto tarefaCurrent = tarefaService.updateTarefa(tarefaDtoMock);

        assertEquals(tarefaDtoMock, tarefaCurrent);
        verify(tarefaRepository).findByIdAndDeletadoFalse(tarefaDtoMock.getId());
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve retornar Id da tarefa não encontrado ao atualizar a tarefa")
    void whenRequestUpdateTarefaThenItShouldReturnNotFound() {
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();

        when(tarefaRepository.findByIdAndDeletadoFalse(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseNotFoundException.class,
                () -> tarefaService.updateTarefa(tarefaDtoMock));

        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve retornar Id do usuário não encontrado ao atualizar a tarefa")
    void whenRequestUpdateTarefaUserThenItShouldReturnNotFound() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();

        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaDtoMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioDto());

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaDtoMock.getId()))
                .thenReturn(Optional.of(tarefaModelMock));

        when(usuarioService.findByIdUsuario(anyLong()))
                .thenThrow(ResponseNotFoundException.class);

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.EMPTY_LIST);

        Assertions.assertThrows(ResponseNotFoundException.class,
                () -> tarefaService.updateTarefa(tarefaDtoMock));

        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Não deve atualizar uma tarefa com o nome já cadastro")
    void whenRequestUpdateTarefaThenItShouldReturnUnProcessable() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaModelMock.setId(2L);

        when(tarefaRepository.findByIdAndDeletadoFalse(anyLong()))
                .thenReturn(Optional.of(tarefaModelMock));

        when(tarefaRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.singletonList(tarefaModelMock));

        Assertions.assertThrows(ResponseUnprocessableException.class,
                () -> tarefaService.updateTarefa(tarefaDtoMock));

        verifyNoMoreInteractions(tarefaRepository);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    @DisplayName("Deve retornar que o Id não foi encontrado ao deletar uma tarefa")
    void whenRequestDeleteTarefaThenItShouldReturnNotFound() {
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();

        when(tarefaRepository.findByIdAndDeletadoFalse(anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseNotFoundException.class,
                () -> tarefaService.deleteTarefa(tarefaDtoMock.getId()));

        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve deletar uma tarefa")
    void whenRequestDeleteTarefaThenItShouldReturnDeleted() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();

        when(tarefaRepository.findByIdAndDeletadoFalse(tarefaDtoMock.getId()))
                .thenReturn(Optional.of(tarefaModelMock));

        when(tarefaRepository.findByTarefaPrincipal(tarefaModelMock))
                .thenReturn(Collections.singletonList(tarefaModelMock));

        when(tarefaRepository.saveAll(any()))
                .thenReturn(Collections.singletonList(tarefaModelMock));

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenReturn(tarefaModelMock);

        MessageResponseDto message = tarefaService.deleteTarefa(tarefaDtoMock.getId());

        assertNotNull(message);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas pelo filtro de data")
    void whenRequestFilterDateTarefaThenItShouldReturnListTarefa() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        List<TarefaDto> listExpected = Collections.singletonList(TarefaBuilder.createTarefaDto());
        LocalDateTime initialDate =
                LocalDateTime.of(2023, 10, 5, 0,0,0);
        LocalDateTime finalDate =
                LocalDateTime.of(2023, 10, 10, 23,59,59);

        LocalDate in1 = LocalDate.of(2023, 10, 5);
        LocalDate in2 =
                LocalDate.of(2023, 10, 10);

        when(tarefaRepository.findByDeletadoFalseAndCreateAtBetween(
                initialDate, finalDate))
                .thenReturn(Collections.singletonList(tarefaModelMock));

        List<TarefaDto> response = tarefaService.filterDate(in1, in2);


        assertEquals(listExpected, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas vazia no filtro de data")
    void whenRequestFilterDateTarefaThenItShouldReturnListEmpty() {

        LocalDateTime initialDate =
                LocalDateTime.of(2023, 10, 5, 0,0,0);
        LocalDateTime finalDate =
                LocalDateTime.of(2023, 10, 10, 23,59,59);

        LocalDate in1 = LocalDate.of(2023, 10, 5);
        LocalDate in2 =
                LocalDate.of(2023, 10, 10);

        when(tarefaRepository.findByDeletadoFalseAndCreateAtBetween(
                initialDate, finalDate))
                .thenReturn(Collections.EMPTY_LIST);

        List<TarefaDto> response = tarefaService.filterDate(in1, in2);


        assertEquals(Collections.EMPTY_LIST, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas no filtro tarefas atrasadas")
    void whenRequestFilterTarefaAtrasadaThenItShouldReturnListTarefa() {
        List<TarefaModel> listTarefaModelMock = Collections
                .singletonList(TarefaBuilder.createTarefaModel());

        List<TarefaDto> listExpectedMock = Collections
                .singletonList(TarefaBuilder.createTarefaDto());

        LocalDate date = LocalDate.now();
        LocalDateTime finalCurrentDate = LocalDateTime.of(
                date.getYear(),
                date.getMonth(),
                date.getDayOfMonth(),
                23,59,59);

        when(tarefaRepository.findAllTarefaAtrasada(finalCurrentDate))
                .thenReturn(listTarefaModelMock);

        List<TarefaDto> response = tarefaService.listAllTarefaAtrasada();


        assertEquals(listExpectedMock, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas vazia no tarefa atrasada")
    void whenRequestFilterTarefaAtrasadaThenItShouldReturnListEmpty() {
        LocalDate date = LocalDate.now();
        LocalDateTime finalCurrentDate = LocalDateTime.of(
                date.getYear(),
                date.getMonth(),
                date.getDayOfMonth(),
                23,59,59);

        when(tarefaRepository.findAllTarefaAtrasada(finalCurrentDate))
                .thenReturn(Collections.EMPTY_LIST);

        List<TarefaDto> response = tarefaService.listAllTarefaAtrasada();

        assertEquals(Collections.EMPTY_LIST, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas por usuário")
    void whenRequestFilterUserThenItShouldReturnListTarefa() {
        TarefaModel tarefaModelMock = TarefaBuilder.createTarefaModel();
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaDtoMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioDto());
        List<TarefaDto> listExpected = Collections.singletonList(TarefaBuilder.createTarefaDto());


        when(tarefaRepository.listTarefaByUsuario(anyLong()))
                .thenReturn(Collections.singletonList(tarefaModelMock));

        List<TarefaDto> response = tarefaService
                .listTarefaByUsuario(tarefaDtoMock.getUsuarioResponsavel().getId());

        assertEquals(listExpected, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia de tarefas por usuário")
    void whenRequestFilterUserThenItShouldReturnListTarefaEmpty() {
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();
        tarefaDtoMock.setUsuarioResponsavel(UsuarioBuilder.createUsuarioDto());

        when(tarefaRepository.listTarefaByUsuario(anyLong()))
                .thenReturn(Collections.EMPTY_LIST);

        List<TarefaDto> response = tarefaService
                .listTarefaByUsuario(tarefaDtoMock.getUsuarioResponsavel().getId());

        assertEquals(Collections.EMPTY_LIST, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista de tarefas na busca de usuário e status")
    void whenRequestFilterUserAndStatusThenItShouldReturnListTarefa() {
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();

        List<TarefaDto> listExpectedMock = Collections
                .singletonList(TarefaBuilder.createTarefaDto());

        when(tarefaRepository
                .listTarefaByUsuarioAndStatus(tarefaDtoMock.getId(), SituacaoTarefaEnum.INICIADA.situacao))
                .thenReturn(Collections.singletonList(TarefaBuilder.createTarefaModel()));

        List<TarefaDto> response = tarefaService
                .listTarefaByUsuarioAndStatus(
                        tarefaDtoMock.getId(),
                        SituacaoTarefaEnum.valueOf("INICIADA"));

        assertEquals(listExpectedMock, response);
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia de tarefas na busca de usuário e status")
    void whenRequestFilterUserAndStatusThenItShouldReturnListTarefaEmpty() {
        TarefaDto tarefaDtoMock = TarefaBuilder.createTarefaDto();

        when(tarefaRepository
                .listTarefaByUsuarioAndStatus(tarefaDtoMock.getId(), SituacaoTarefaEnum.ENCERRADA.situacao))
                .thenReturn(Collections.EMPTY_LIST);

        List<TarefaDto> response = tarefaService
                .listTarefaByUsuarioAndStatus(
                        tarefaDtoMock.getId(),
                        SituacaoTarefaEnum.valueOf("ENCERRADA"));

        assertEquals(Collections.EMPTY_LIST, response);
        verifyNoMoreInteractions(tarefaRepository);
    }
}
