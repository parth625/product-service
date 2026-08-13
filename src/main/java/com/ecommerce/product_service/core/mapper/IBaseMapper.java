package com.ecommerce.product_service.core.mapper;

import org.mapstruct.Context;

public interface IBaseMapper<DTO, DATA> {
    DTO domainToDTO(DATA data, @Context CycleAvoidingMappingContext context);
    DATA dtoToDomain(DTO dtoData, @Context CycleAvoidingMappingContext context);
}
