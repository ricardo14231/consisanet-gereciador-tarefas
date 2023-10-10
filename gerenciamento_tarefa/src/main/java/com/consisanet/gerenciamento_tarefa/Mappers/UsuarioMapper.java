package com.consisanet.gerenciamento_tarefa.Mappers;

import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.models.UsuarioModel;

public class UsuarioMapper {

    public static UsuarioDto toDto(UsuarioModel usuarioModel) {
        UsuarioDto usuarioDto = new UsuarioDto();

        usuarioDto.setId(usuarioModel.getId());
        usuarioDto.setNome(usuarioModel.getNome());
        //usuarioDto.setTarefasResponsaveis(usuarioModel.getTarefasResponsaveis());
        usuarioDto.setUpdateAt(usuarioModel.getUpdateAt());
        usuarioDto.setCreateAt(usuarioModel.getCreateAt());

        return usuarioDto;
    }

    public static UsuarioModel toModel(UsuarioDto usuarioDto) {
        UsuarioModel usuarioModel = new UsuarioModel();

        usuarioModel.setId(usuarioDto.getId());
        usuarioModel.setNome(usuarioDto.getNome());
        //usuarioDto.setTarefasResponsaveis(usuarioModel.getTarefasResponsaveis());

        return usuarioModel;
    }
}
