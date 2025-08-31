package com.gustavo.neoappcustomerapi.business;

import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.business.mapper.ClienteMapper;
import com.gustavo.neoappcustomerapi.infrastructure.entity.Cliente;
import com.gustavo.neoappcustomerapi.infrastructure.exception.ConflictException;
import com.gustavo.neoappcustomerapi.infrastructure.exception.UnauthorizedException;
import com.gustavo.neoappcustomerapi.infrastructure.repository.ClienteRepository;
import com.gustavo.neoappcustomerapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public ClienteDTO salvaUsuario(ClienteDTO clienteDto) {
        emailExiste(clienteDto.getEmail());
        clienteDto.setSenha(passwordEncoder.encode(clienteDto.getSenha()));
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        return clienteMapper.toDTO(
                clienteRepository.save(cliente)
        );
    }

    public String autenticarUsuario(ClienteDTO clienteDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(clienteDTO.getEmail(),
                            clienteDTO.getSenha())
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());

        } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e) {
            throw new UnauthorizedException("Usuário ou senha inválidos: ", e.getCause());
        }
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return clienteRepository.existsByEmail(email);
    }
}
