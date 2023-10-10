package com.consisanet.gerenciamento_tarefa.controllers;

import com.consisanet.gerenciamento_tarefa.dtos.MessageResponseDto;
import com.consisanet.gerenciamento_tarefa.dtos.TarefaDto;
import com.consisanet.gerenciamento_tarefa.enums.SituacaoTarefaEnum;
import com.consisanet.gerenciamento_tarefa.services.TarefaService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/tarefa")
@Api(value = "Endpoints para gerenciamento de tarefas.")
public class TarefaController {

    @Autowired
    TarefaService taferaService;

    @ApiOperation(value = "Lista todas as tarefas.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao retornar a lista de tarefas.")
            }
    )
    @GetMapping("/listAll")
    public ResponseEntity<List<TarefaDto>> listAllTarefa() {
        return new ResponseEntity<>(taferaService.listAllTarefa(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma tarefa pelo Id.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao retornar a tarefa pelo Id."),
            }
    )
    @GetMapping({"/{id}"})
    public ResponseEntity<TarefaDto> findByIdTarefa(@PathVariable Long id) {
        return new ResponseEntity<>(taferaService.findByIdTarefa(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Cadastra uma tarefa.", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Sucesso ao cadastrar uma nova tarefa."),
                    @ApiResponse(code = 400, message = "Erro com a requisição para criar a tarefa."),
                    @ApiResponse(code = 404, message = "Id do usuário informado não encontrado.")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<TarefaDto> createTarefa(@Valid @RequestBody TarefaDto tarefaDto) {
        return new ResponseEntity<>(taferaService.createTarefa(tarefaDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza uma tarefa.", httpMethod = "PUT")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao atualizar a tarefa."),
                    @ApiResponse(code = 400, message = "Erro com a requisição para atualizar a tarefa."),
                    @ApiResponse(code = 404, message = "Tarefa com Id informado não encontrada."),
                    @ApiResponse(code = 404, message = "Id do usuário informado não encontrado.")
            }
    )
    @PutMapping("/update")
    public ResponseEntity<TarefaDto> updateTarefa(@Valid @RequestBody TarefaDto tarefaDto) {
        return new ResponseEntity<>(taferaService.updateTarefa(tarefaDto), HttpStatus.OK);
    }


    @ApiOperation(value = "Deleta uma tarefa.", httpMethod = "DELETE")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao deletar a tarefa."),
                    @ApiResponse(code = 404, message = "Tarefa com Id informado não encontrada.")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponseDto> deleteTarefa(@PathVariable Long id) {
        return new ResponseEntity<>(taferaService.deleteTarefa(id), HttpStatus.OK);
    }

    //filtro por data início e fim das tarefas

    @ApiOperation(value = "Filtra uma tarefa pela data inicial e final da tarefa.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao consultar as tarefas."),
                    @ApiResponse(code = 400, message = "Erro no filtro informado.")
            }
    )
    @GetMapping("/filter/date/{initialDate}/{finalDate}")
    public ResponseEntity<List<TarefaDto>> filterDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate initialDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate finalDate) {
        return new ResponseEntity<>(taferaService.filterDate(initialDate, finalDate), HttpStatus.OK);
    }

    //filtro de tarefas atrasadas
    @ApiOperation(value = "Filtra as tarefas que estão atrasadas.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao consultar as tarefas.")
            }
    )
    @GetMapping("/filter/late")
    public ResponseEntity<List<TarefaDto>> listAllTarefaAtrasada() {
        return new ResponseEntity<>(taferaService.listAllTarefaAtrasada(), HttpStatus.OK);
    }

    //Filtra tarefas pelo usuário e status.
    @ApiOperation(value = "Filtra as tarefas pelo usuário e status da tarefa.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao consultar as tarefas."),
                    @ApiResponse(code = 400, message = "Erro com os dados do filtro informado."),
                    @ApiResponse(code = 404, message = "Usuário com Id informado não encontrado.")
            }
    )
    @GetMapping("/filter/{usuarioId}/{statusTarefa}")
    public ResponseEntity<List<TarefaDto>> listTarefaByUsuarioAndStatus(
            @PathVariable Long usuarioId,
            @PathVariable SituacaoTarefaEnum statusTarefa
    ) {
        return new ResponseEntity<>(taferaService.listTarefaByUsuarioAndStatus(
                usuarioId,
                statusTarefa
        ), HttpStatus.OK);
    }

    //filtro para exibir todas as tarefas vinculadas a cada usuário
    @ApiOperation(value = "Filtra as tarefas pelo usuário.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao consultar as tarefas."),
                    @ApiResponse(code = 400, message = "Erro com os dados do filtro informado."),
                    @ApiResponse(code = 404, message = "Usuário com Id informado não encontrado.")
            }
    )
    @GetMapping("/filter/{usuarioId}")
    public ResponseEntity<List<TarefaDto>> listTarefaByUsuario(
            @PathVariable Long usuarioId) {
        return new ResponseEntity<>(taferaService.listTarefaByUsuario(
                usuarioId), HttpStatus.OK);
    }
}
