package com.ecommerce.product_service.core.service;

import com.ecommerce.product_service.core.dto.GenericDataDto;
import com.ecommerce.product_service.core.dto.PaginationRequestDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ExBaseService<DTO, ID> {
    List<DTO> getAllEntities(HttpServletRequest request);
    DTO getEntityById(ID id, HttpServletRequest request);
    DTO saveEntity(DTO entity, HttpServletRequest request);
    DTO updateEntity(ID id, DTO entity, HttpServletRequest request);
    DTO deleteEntity(ID id, HttpServletRequest request);
    GenericDataDto getListByPagination(PaginationRequestDto paginationRequest, HttpServletRequest request);
}
