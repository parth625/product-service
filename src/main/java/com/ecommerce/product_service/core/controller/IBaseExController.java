package com.ecommerce.product_service.core.controller;

import com.ecommerce.product_service.core.dto.GenericDataDto;
import com.ecommerce.product_service.core.dto.PaginationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface IBaseExController<DTO> {
    ResponseEntity<GenericDataDto> getAllWithoutPagination(HttpServletRequest request);
    ResponseEntity<GenericDataDto> getAllWithPagination(PaginationRequestDto requestDTO, HttpServletRequest request);
    ResponseEntity<GenericDataDto> getEntityById(Long id, HttpServletRequest request);
    ResponseEntity<GenericDataDto> save(DTO entityDTO, BindingResult result, HttpServletRequest request);
    ResponseEntity<GenericDataDto> update(Long id, DTO entityDTO, BindingResult result, HttpServletRequest request);
    ResponseEntity<GenericDataDto> delete(Long id, HttpServletRequest request);
}
