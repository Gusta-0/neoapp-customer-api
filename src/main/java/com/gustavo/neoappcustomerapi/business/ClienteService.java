package com.gustavo.neoappcustomerapi.business;


import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.business.dto.ClienteLoginDTO;

import com.gustavo.neoappcustomerapi.business.dto.ClienteResponseDTO;
import com.gustavo.neoappcustomerapi.business.mapper.ClienteMapper;
import com.gustavo.neoappcustomerapi.business.mapper.ClienteUpdateMapper;
import com.gustavo.neoappcustomerapi.infrastructure.entity.Cliente;
import com.gustavo.neoappcustomerapi.infrastructure.exception.ConflictException;
import com.gustavo.neoappcustomerapi.infrastructure.exception.ResourceNotFoundException;
import com.gustavo.neoappcustomerapi.infrastructure.exception.UnauthorizedException;
import com.gustavo.neoappcustomerapi.infrastructure.repository.ClienteRepository;
import com.gustavo.neoappcustomerapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ClienteUpdateMapper clienteUpdateMapper;

    public ClienteResponseDTO salvaCliente(ClienteDTO clienteDto) {
        emailExiste(clienteDto.getEmail());
        clienteDto.setSenha(passwordEncoder.encode(clienteDto.getSenha()));

        Cliente cliente = clienteMapper.toEntity(clienteDto);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(clienteSalvo);
    }


    public String autenticarCliente(ClienteLoginDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(),
                            dto.getSenha())
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

    public Page<ClienteResponseDTO> listarClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(clienteMapper::toResponseDTO);
    }


    public Page<ClienteResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(clienteMapper::toResponseDTO);
    }

    public Page<ClienteResponseDTO> buscarPorEmail(String email, Pageable pageable) {
        return clienteRepository.findByEmailContainingIgnoreCase(email, pageable)
                .map(clienteMapper::toResponseDTO);
    }

    public ClienteResponseDTO atualizaDadosCliente(ClienteDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Cliente clienteExistente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente com e-mail " + email + " não localizado"));

        if (StringUtils.hasText(dto.getSenha())) {
            dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        } else {
            dto.setSenha(null);
        }

        clienteUpdateMapper.updateCliente(dto, clienteExistente);

        Cliente atualizado = clienteRepository.save(clienteExistente);
        return clienteMapper.toResponseDTO(atualizado);
    }


    private boolean verificaCpfExistente(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .isPresent();
    }

    @Transactional
    public String deletaClientePorEmail(String email) {
        clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email));
        clienteRepository.deleteByEmail(email);
        return "Usuário deletado com sucesso: " + email;
    }
}