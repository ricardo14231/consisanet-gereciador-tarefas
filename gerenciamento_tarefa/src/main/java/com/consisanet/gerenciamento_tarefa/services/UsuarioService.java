package com.consisanet.gerenciamento_tarefa.services;

import com.consisanet.gerenciamento_tarefa.Mappers.UsuarioMapper;
import com.consisanet.gerenciamento_tarefa.dtos.MessageResponseDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.exceptions.ResponseNotFoundException;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;
import com.consisanet.gerenciamento_tarefa.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDto findByIdUsuario(Long id) {
        return UsuarioMapper.toDto(verifyIfExists(id));
    }

    public UsuarioDto createUsuario(UsuarioDto usuarioDto) {
        UsuarioModel usuarioToSave = UsuarioMapper.toModel(usuarioDto);

        return UsuarioMapper.toDto(usuarioRepository.save(usuarioToSave));
    }

    public List<UsuarioDto> listAllUsuario() {
        return usuarioRepository.findAllByDeletadoFalse().stream()
                .map(UsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public MessageResponseDto updateUsuario(UsuarioDto usuarioDto) {
        verifyIfExists(usuarioDto.getId());

        UsuarioModel usuarioToSave = UsuarioMapper.toModel(usuarioDto);

        usuarioToSave.setNome(usuarioDto.getNome());
        usuarioToSave.setUpdateAt(LocalDateTime.now());

        usuarioRepository.save(usuarioToSave);

        return new MessageResponseDto("Usuário com Id: " + usuarioDto.getId() + " atualizado com sucesso");
    }

    public MessageResponseDto deleteUsuario(Long id) {
        UsuarioModel usuarioToDelete = verifyIfExists(id);

        usuarioToDelete.setDeletado(true);
        usuarioToDelete.setUpdateAt(LocalDateTime.now());

        usuarioRepository.save(usuarioToDelete);

        return new MessageResponseDto("Recurso com Id: " + id + " deletado");
    }

    private UsuarioModel verifyIfExists(Long id) {
        return usuarioRepository.findByIdAndDeletadoFalse(id)
                .orElseThrow(() -> new ResponseNotFoundException("Usuário com Id: " + id + " não encontrada"));
    }

}
