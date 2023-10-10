package com.consisanet.gerenciamento_tarefa.controllers;

import com.consisanet.gerenciamento_tarefa.dtos.MessageResponseDto;
import com.consisanet.gerenciamento_tarefa.dtos.UsuarioDto;
import com.consisanet.gerenciamento_tarefa.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/usuario")
@Api(value = "Endpoints para gerenciamento de usuários.")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @ApiOperation(value = "Lista todos os usuários.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao retornar a lista de usuários.")
            }
    )
    @GetMapping("/listAll")
    public ResponseEntity<List<UsuarioDto>> listAllUsuario()  {
        return new ResponseEntity<>(usuarioService.listAllUsuario(), HttpStatus.OK);
    }


    @ApiOperation(value = "Retorna uma usuário pelo Id.", httpMethod = "GET")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao retornar o usuário pelo Id.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findByIdUsuario(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.findByIdUsuario(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Cadastra um usuário.", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Sucesso ao cadastrar o usuário."),
                    @ApiResponse(code = 400, message = "Erro com a requisição para criar o usuário.")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<UsuarioDto> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        return new ResponseEntity<>(usuarioService.createUsuario(usuarioDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza um usuário.", httpMethod = "PUT")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao atualizar o usuário."),
                    @ApiResponse(code = 400, message = "Erro com a requisição para atualizar o usuário."),
                    @ApiResponse(code = 404, message = "Usuário com Id informado não encontrado.")
            }
    )
    @PutMapping("/update")
    public ResponseEntity<MessageResponseDto> updateUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        return new ResponseEntity<>(usuarioService.updateUsuario(usuarioDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Deleta um usuário.", httpMethod = "DELETE")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso a deletar o usuário."),
                    @ApiResponse(code = 404, message = "Usuário com Id informado não encontrado.")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponseDto> deleteUsuario(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.deleteUsuario(id), HttpStatus.OK);
    }

}
