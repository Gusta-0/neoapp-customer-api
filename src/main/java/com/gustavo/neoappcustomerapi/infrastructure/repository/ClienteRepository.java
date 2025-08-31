package com.gustavo.neoappcustomerapi.infrastructure.repository;

import com.gustavo.neoappcustomerapi.infrastructure.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}

