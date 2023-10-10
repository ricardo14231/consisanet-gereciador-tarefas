package com.consisanet.gerenciamento_tarefa.Mappers;

import com.consisanet.gerenciamento_tarefa.dtos.TarefaDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.models.TarefaModel;

public class TarefaMapper {

    public static TarefaDto toDto(TarefaModel tarefaModel) {
        TarefaDto tarefaDto = new TarefaDto();

        tarefaDto.setId(tarefaModel.getId());
        tarefaDto.setNomeTarefa(tarefaModel.getNomeTarefa());
        tarefaDto.setDataInicio(tarefaModel.getDataInicio());
        tarefaDto.setDataFim(tarefaModel.getDataFim());

        if(tarefaModel.getUsuarioResponsavel() != null)
            tarefaDto.setUsuarioResponsavel(UsuarioMapper.toDto(tarefaModel.getUsuarioResponsavel()));

        if(tarefaModel.getTarefaPrincipal() != null)
            tarefaDto.setTarefaPrincipal(toDtoSubTarefa(tarefaModel.getTarefaPrincipal()));
        tarefaDto.setSituacaoTarefa(tarefaModel.getSituacaoTarefa());
        tarefaDto.setUpdateAt(tarefaModel.getUpdateAt());
        tarefaDto.setCreateAt(tarefaModel.getCreateAt());

        return tarefaDto;
    }

    public static TarefaModel toModel(TarefaDto tarefaDto) {
        TarefaModel tarefaModel = new TarefaModel();

        tarefaModel.setId(tarefaDto.getId());
        tarefaModel.setNomeTarefa(tarefaDto.getNomeTarefa());
        tarefaModel.setDataInicio(tarefaDto.getDataInicio());
        tarefaModel.setDataFim(tarefaDto.getDataFim());
        if(tarefaDto.getUsuarioResponsavel() != null)
            tarefaModel.setUsuarioResponsavel(UsuarioMapper.toModel(tarefaDto.getUsuarioResponsavel()));
        tarefaModel.setSituacaoTarefa(tarefaDto.getSituacaoTarefa());

        return tarefaModel;
    }

    private static TarefaDto toDtoSubTarefa(TarefaModel tarefaModel) {
        TarefaDto tarefaDto = new TarefaDto();

        tarefaDto.setId(tarefaModel.getId());
        tarefaDto.setNomeTarefa(tarefaModel.getNomeTarefa());
        tarefaDto.setDataInicio(tarefaModel.getDataInicio());
        tarefaDto.setDataFim(tarefaModel.getDataFim());

        if(tarefaModel.getUsuarioResponsavel() != null)
            tarefaDto.setUsuarioResponsavel(UsuarioMapper.toDto(tarefaModel.getUsuarioResponsavel()));

        tarefaDto.setSituacaoTarefa(tarefaModel.getSituacaoTarefa());
        tarefaDto.setUpdateAt(tarefaModel.getUpdateAt());
        tarefaDto.setCreateAt(tarefaModel.getCreateAt());

        return tarefaDto;
    }

    public static TarefaModel toModelSubTarefa(TarefaDto tarefaDto) {
        TarefaModel tarefaModel = new TarefaModel();

        tarefaModel.setId(tarefaDto.getId());
            tarefaModel.setNomeTarefa(tarefaDto.getNomeTarefa());
        tarefaModel.setDataInicio(tarefaDto.getDataInicio());
        tarefaModel.setDataFim(tarefaDto.getDataFim());
        if(tarefaDto.getUsuarioResponsavel() != null)
            tarefaModel.setUsuarioResponsavel(UsuarioMapper.toModel(tarefaDto.getUsuarioResponsavel()));

        if(tarefaDto.getTarefaPrincipal() != null)
            tarefaModel.setTarefaPrincipal(toModelSubTarefa(tarefaDto.getTarefaPrincipal()));

        tarefaModel.setSituacaoTarefa(tarefaDto.getSituacaoTarefa());

        return tarefaModel;
    }
}
