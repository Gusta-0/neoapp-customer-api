package com.gustavo.neoappcustomerapi.infrastructure.config;

import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.business.dto.ClienteLoginDTO;
import com.gustavo.neoappcustomerapi.business.dto.ClienteResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clientes", description = "API de gerenciamento de clientes")
public interface ClienteAPI {

    @Operation(summary = "Salvar novo cliente", 
            description = "Cria um novo registro de cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso",
                content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<ClienteResponseDTO> salvaUsuario(@Valid @RequestBody ClienteDTO clienteDTO);

    @Operation(summary = "Login do cliente", 
            description = "Autenticar cliente e retornar token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    String login(@Valid @RequestBody ClienteLoginDTO dto);

    @Operation(summary = "Listar todos os clientes", 
            description = "Retorna uma lista paginada de clientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<Page<ClienteResponseDTO>> listarClientes(
        @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "nome") String sortBy
    );

    @Operation(summary = "Buscar clientes", 
            description = "Busca clientes por nome ou email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    ResponseEntity<Page<ClienteResponseDTO>> buscarClientes(
        @Parameter(description = "Nome do cliente") @RequestParam(required = false) String nome,
        @Parameter(description = "Email do cliente") @RequestParam(required = false) String email,
        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Atualizar cliente", 
            description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<ClienteResponseDTO> atualizaDadoUsuario(@Valid @RequestBody ClienteDTO dto);

    @Operation(summary = "Deletar cliente", 
            description = "Remove um cliente do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<Void> deletaUsuario(
        @Parameter(description = "Email do cliente") @PathVariable String email
    );
}