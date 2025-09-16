package com.gustavo.neoappcustomerapi.business.mapper;

import com.gustavo.neoappcustomerapi.business.dto.ClienteDTO;
import com.gustavo.neoappcustomerapi.infrastructure.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClienteUpdateMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateCliente(ClienteDTO dto, @MappingTarget Cliente entity);
}

