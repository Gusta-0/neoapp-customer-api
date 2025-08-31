package com.gustavo.neoappcustomerapi.business.mapper;

import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.infrastructure.entity.Cliente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDTO toDTO(Cliente cliente);
    Cliente toEntity(ClienteDTO clienteDTO);

    List<ClienteDTO> toDTOList(List<Cliente> clientes);

    List<Cliente> toEntityList(List<ClienteDTO> clienteDTOs);
}
