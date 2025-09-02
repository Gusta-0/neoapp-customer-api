package com.gustavo.neoappcustomerapi.controller;

import com.gustavo.neoappcustomerapi.business.ClienteService;
import com.gustavo.neoappcustomerapi.business.dto.*;

import com.gustavo.neoappcustomerapi.infrastructure.config.ClienteAPI;
import com.gustavo.neoappcustomerapi.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
@Validated
public class ClienteController implements ClienteAPI {

    private final ClienteService clienteService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvaUsuario(@Validated(OnCreate.class) @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.salvaCliente(clienteDTO));
    }

    @Override
    @PostMapping("/login")
    public String login(@Valid @RequestBody ClienteLoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );
        return jwtUtil.generateToken(authentication.getName());
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listarClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(clienteService.listarClientes(pageable));
    }

    @Override
    @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> buscarClientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (nome != null) {
            return ResponseEntity.ok(clienteService.buscarPorNome(nome, pageable));
        } else if (email != null) {
            return ResponseEntity.ok(clienteService.buscarPorEmail(email, pageable));
        } else {
            return ResponseEntity.ok(clienteService.listarClientes(pageable));
        }
    }

    @Override
    @PatchMapping
    public ResponseEntity<ClienteResponseDTO> atualizaDadoUsuario(@Validated(OnUpdate.class) @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.atualizaDadosCliente(dto));
    }

    @Override
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuario(@PathVariable String email) {
        clienteService.deletaClientePorEmail(email);
        return ResponseEntity.ok().build();
    }
}
