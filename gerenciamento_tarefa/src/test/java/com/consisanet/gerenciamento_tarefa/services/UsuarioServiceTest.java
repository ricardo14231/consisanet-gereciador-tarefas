package com.consisanet.gerenciamento_tarefa.services;

import com.consisanet.gerenciamento_tarefa.dtos.MessageResponseDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.exceptions.ResponseNotFoundException;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;
import com.consisanet.gerenciamento_tarefa.repositorys.UsuarioRepository;
import com.consisanet.gerenciamento_tarefa.utils.UsuarioBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve listar todos os usuários")
    void whenResquetListUserItShouldReturnListUser() {
        List<UsuarioModel> listUsuarioMock =
                Collections.singletonList(UsuarioBuilder.createUsuarioModel());

        List<UsuarioDto> listExpected = Collections.singletonList(UsuarioBuilder.createUsuarioDto());

        when(usuarioRepository.findAllByDeletadoFalse())
                .thenReturn(listUsuarioMock);

        List<UsuarioDto> responseList = usuarioService.listAllUsuario();

        assertEquals(listExpected, responseList);
        verify(usuarioRepository).findAllByDeletadoFalse();
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia de usuários")
    void whenResquetListUserItShouldReturnListEmpty() {

        when(usuarioRepository.findAllByDeletadoFalse())
                .thenReturn(Collections.EMPTY_LIST);

        List<UsuarioDto> responseList = usuarioService.listAllUsuario();

        assertEquals(Collections.EMPTY_LIST, responseList);
        verify(usuarioRepository).findAllByDeletadoFalse();
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar um usuário pelo Id")
    void whenResquetUserByIdItShouldReturnUser() {
        UsuarioModel userModelMock = UsuarioBuilder.createUsuarioModel();
        UsuarioDto userExpected = UsuarioBuilder.createUsuarioDto();

        when(usuarioRepository.findByIdAndDeletadoFalse(userModelMock.getId()))
                .thenReturn(Optional.of(userModelMock));

        UsuarioDto response = usuarioService.findByIdUsuario(userExpected.getId());

        assertEquals(userExpected, response);
        verify(usuarioRepository).findByIdAndDeletadoFalse(userModelMock.getId());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar Id do usuário não encontrado")
    void whenResquetUserByIdItShouldReturnException() {
        UsuarioModel userModelMock = UsuarioBuilder.createUsuarioModel();

        when(usuarioRepository.findByIdAndDeletadoFalse(userModelMock.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResponseNotFoundException.class,
                () -> usuarioService.findByIdUsuario(userModelMock.getId()));

        verify(usuarioRepository).findByIdAndDeletadoFalse(userModelMock.getId());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve criar um usuário")
    void whenResquetCreateUserItShouldReturnCreated() {
        UsuarioModel userModelMock = UsuarioBuilder.createUsuarioModel();
        UsuarioDto userExpected = UsuarioBuilder.createUsuarioDto();

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenReturn(userModelMock);

        UsuarioDto response = usuarioService.createUsuario(userExpected);

        assertEquals(userExpected, response);
        verify(usuarioRepository).save(any(UsuarioModel.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve atualizar um usuário")
    void whenResquetUpdateUserItShouldReturnUser() {
        UsuarioModel userModelMock = UsuarioBuilder.createUsuarioModel();
        UsuarioDto userExpected = UsuarioBuilder.createUsuarioDto();

        MessageResponseDto msg =
                new MessageResponseDto("Usuário com Id: " + userModelMock.getId() + " atualizado com sucesso");

        when(usuarioRepository.findByIdAndDeletadoFalse(userExpected.getId()))
                .thenReturn(Optional.of(userModelMock));

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenReturn(userModelMock);

        MessageResponseDto response = usuarioService.updateUsuario(userExpected);

        assertEquals(msg, response);
        verify(usuarioRepository).save(any(UsuarioModel.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Não deve atualizar um usuário sem cadastro")
    void whenResquetUpdateUserItShouldReturnException() {
        UsuarioDto userExpected = UsuarioBuilder.createUsuarioDto();

        when(usuarioRepository.findByIdAndDeletadoFalse(userExpected.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResponseNotFoundException.class,
                () -> usuarioService.updateUsuario(userExpected));
        verify(usuarioRepository).findByIdAndDeletadoFalse(userExpected.getId());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Deve deletar um usuário")
    void whenResquetDeleteUserItShouldReturnDeleted() {
        UsuarioModel userModelMock = UsuarioBuilder.createUsuarioModel();

        MessageResponseDto msg =
                new MessageResponseDto("Recurso com Id: " + userModelMock.getId() + " deletado");

        when(usuarioRepository.findByIdAndDeletadoFalse(userModelMock.getId()))
                .thenReturn(Optional.of(userModelMock));

        when(usuarioRepository.save(any(UsuarioModel.class)))
                .thenReturn(userModelMock);

        MessageResponseDto response = usuarioService.deleteUsuario(userModelMock.getId());

        assertEquals(msg, response);
        verify(usuarioRepository).findByIdAndDeletadoFalse(userModelMock.getId());
        verify(usuarioRepository).save(any(UsuarioModel.class));
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Não deve deletar um usuário sem cadastro")
    void whenResquetDeleteUserItShouldReturnException() {

        UsuarioDto userExpected = UsuarioBuilder.createUsuarioDto();

        when(usuarioRepository.findByIdAndDeletadoFalse(userExpected.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResponseNotFoundException.class,
                () -> usuarioService.deleteUsuario(userExpected.getId()));
        verify(usuarioRepository).findByIdAndDeletadoFalse(userExpected.getId());
        verifyNoMoreInteractions(usuarioRepository);
    }
}
