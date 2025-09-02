package com.gustavo.neoappcustomerapi.business.mapper;

import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.business.dto.ClienteResponseDTO;
import com.gustavo.neoappcustomerapi.infrastructure.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponseDTO toResponseDTO(Cliente cliente);
    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes);

    @Mapping(target = "id", ignore = true)
    Cliente toEntity(ClienteDTO requestDTO);
    List<Cliente> toEntityList(List<ClienteDTO> requestDTOs);

    @AfterMapping
    default void calcularIdade(@MappingTarget ClienteResponseDTO responseDTO, Cliente cliente) {
        if (cliente.getDataNascimento() != null) {
            responseDTO.setIdade(Period.between(cliente.getDataNascimento(), LocalDate.now()).getYears());
        }
    }
}