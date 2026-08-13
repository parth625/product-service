package com.ecommerce.product_service.core.controller;

import com.ecommerce.product_service.core.dto.GenericDataDto;
import com.ecommerce.product_service.core.dto.IBaseDto;
import com.ecommerce.product_service.core.dto.PaginationRequestDto;
import com.ecommerce.product_service.core.dto.ValidationData;
import com.ecommerce.product_service.core.exceptions.DataNotFoundException;
import com.ecommerce.product_service.core.service.ExBaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
public abstract class ExBaseAbstractController<DTO extends IBaseDto> implements IBaseExController<DTO> {

    private final ExBaseService<DTO, Long> service;

    protected ExBaseAbstractController(ExBaseService<DTO, Long> service) {
        this.service = service;
    }

    public abstract String getModuleNameForLog();

    @Override
    @GetMapping
    public ResponseEntity<GenericDataDto> getAllWithoutPagination(HttpServletRequest request) {
        GenericDataDto response = new GenericDataDto();
        try {
            List<DTO> list = service.getAllEntities(request);
            response.setDataList(list);
            response.setTotalRecords(list.size());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("{}[getAllWithoutPagination] Error: ", getModuleNameForLog(), ex);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage("Error occurred while fetching data");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PostMapping("/page")
    public ResponseEntity<GenericDataDto> getAllWithPagination(@RequestBody PaginationRequestDto requestDTO, HttpServletRequest request) {
        try {
            GenericDataDto response = service.getListByPagination(requestDTO, request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("{}[getAllWithPagination] Error: ", getModuleNameForLog(), ex);
            GenericDataDto response = new GenericDataDto();
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage("Error occurred while fetching paginated data");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<GenericDataDto> getEntityById(@PathVariable("id") Long id, HttpServletRequest request) {
        GenericDataDto response = new GenericDataDto();
        try {
            DTO dto = service.getEntityById(id, request);
            response.setData(dto);
            response.setTotalRecords(1);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException ex) {
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("{}[getEntityById] Error: ", getModuleNameForLog(), ex);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage("Error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<GenericDataDto> save(@Valid @RequestBody DTO entityDTO, BindingResult result, HttpServletRequest request) {
        GenericDataDto response = new GenericDataDto();
        if (result.hasErrors()) {
            response.setResponseCode(HttpStatus.BAD_REQUEST.value());
            response.setResponseMessage(formatValidationErrors(result.getFieldErrors()));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ValidationData validation = validateSave(entityDTO);
        if (!validation.isValid()) {
            response.setResponseCode(HttpStatus.BAD_REQUEST.value());
            response.setResponseMessage(validation.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            DTO saved = service.saveEntity(entityDTO, request);
            response.setData(saved);
            response.setTotalRecords(1);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("{}[save] Error: ", getModuleNameForLog(), ex);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage("Failed to save data");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<GenericDataDto> update(@PathVariable("id") Long id, @Valid @RequestBody DTO entityDTO, BindingResult result, HttpServletRequest request) {
        GenericDataDto response = new GenericDataDto();
        if (result.hasErrors()) {
            response.setResponseCode(HttpStatus.BAD_REQUEST.value());
            response.setResponseMessage(formatValidationErrors(result.getFieldErrors()));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            DTO updated = service.updateEntity(id, entityDTO, request);
            response.setData(updated);
            response.setTotalRecords(1);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException ex) {
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("{}[update] Error: ", getModuleNameForLog(), ex);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage("Failed to update data");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericDataDto> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        GenericDataDto response = new GenericDataDto();
        try {
            DTO deleted = service.deleteEntity(id, request);
            response.setData(deleted);
            response.setTotalRecords(1);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException ex) {
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("{}[delete] Error: ", getModuleNameForLog(), ex);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage("Failed to delete data");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ValidationData validateSave(DTO entityDTO) {
        return new ValidationData();
    }

    protected String formatValidationErrors(List<FieldError> errors) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : errors) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return sb.toString();
    }
}
