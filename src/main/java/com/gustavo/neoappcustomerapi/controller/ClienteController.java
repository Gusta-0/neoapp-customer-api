package com.gustavo.neoappcustomerapi.controller;

import com.gustavo.neoappcustomerapi.business.ClienteService;
import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ClienteDTO> salvaUsuario(@RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.salvaUsuario(clienteDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody ClienteDTO clienteDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(clienteDTO.getEmail(), clienteDTO.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

}
