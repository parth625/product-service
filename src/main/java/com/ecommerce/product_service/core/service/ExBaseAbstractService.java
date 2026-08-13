package com.ecommerce.product_service.core.service;

import com.ecommerce.product_service.core.domain.IBase;
import com.ecommerce.product_service.core.dto.GenericDataDto;
import com.ecommerce.product_service.core.dto.IBaseDto;
import com.ecommerce.product_service.core.dto.PaginationRequestDto;
import com.ecommerce.product_service.core.exceptions.DataNotFoundException;
import com.ecommerce.product_service.core.mapper.CycleAvoidingMappingContext;
import com.ecommerce.product_service.core.mapper.IBaseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Slf4j
public abstract class ExBaseAbstractService<DTO extends IBaseDto, DATA extends IBase<ID>, ID extends Serializable>
        implements ExBaseService<DTO, ID> {

    @Getter
    private final JpaRepository<DATA, ID> repository;
    @Getter
    private final IBaseMapper<DTO, DATA> mapper;

    protected ExBaseAbstractService(JpaRepository<DATA, ID> repository, IBaseMapper<DTO, DATA> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public abstract String getModuleNameForLog();

    @Override
    public List<DTO> getAllEntities(HttpServletRequest request) {
        return repository.findAll().stream()
                .filter(data -> !data.getDeleteFlag())
                .map(data -> mapper.domainToDTO(data, new CycleAvoidingMappingContext()))
                .toList();
    }

    @Override
    public DTO getEntityById(ID id, HttpServletRequest request) {
        DATA entity = repository.findById(id)
                .filter(data -> !data.getDeleteFlag())
                .orElseThrow(() -> new DataNotFoundException(getModuleNameForLog() + " Data not found for id: " + id));
        return mapper.domainToDTO(entity, new CycleAvoidingMappingContext());
    }

    @Override
    public DTO saveEntity(DTO dto, HttpServletRequest request) {
        DATA entity = mapper.dtoToDomain(dto, new CycleAvoidingMappingContext());
        DATA saved = repository.save(entity);
        return mapper.domainToDTO(saved, new CycleAvoidingMappingContext());
    }

    @Override
    public DTO updateEntity(ID id, DTO dto, HttpServletRequest request) {
        // Verify entity exists
        repository.findById(id)
                .filter(data -> !data.getDeleteFlag())
                .orElseThrow(() -> new DataNotFoundException(getModuleNameForLog() + " Data not found for id: " + id));

        DATA entity = mapper.dtoToDomain(dto, new CycleAvoidingMappingContext());
        // Explicitly set the ID from path variable so JPA performs an UPDATE instead of INSERT
        entity.setPrimaryKey(id);
        DATA saved = repository.save(entity);
        return mapper.domainToDTO(saved, new CycleAvoidingMappingContext());
    }

    @Override
    public DTO deleteEntity(ID id, HttpServletRequest request) {
        DATA entity = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(getModuleNameForLog() + " Data not found for id: " + id));
        entity.setDeleteFlag(true);
        DATA saved = repository.save(entity);
        return mapper.domainToDTO(saved, new CycleAvoidingMappingContext());
    }

    @Override
    public GenericDataDto getListByPagination(PaginationRequestDto paginationRequest, HttpServletRequest request) {
        int page = (paginationRequest.getPage() != null && paginationRequest.getPage() > 0) ? paginationRequest.getPage() - 1 : 0;
        int size = (paginationRequest.getPageSize() != null && paginationRequest.getPageSize() > 0) ? paginationRequest.getPageSize() : 10;
        String sortBy = (paginationRequest.getSortBy() != null && !paginationRequest.getSortBy().isBlank()) ? paginationRequest.getSortBy() : "id";
        Sort sort = (paginationRequest.getSortOrder() != null && paginationRequest.getSortOrder() == 2)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Page<DATA> pageResult = repository.findAll(PageRequest.of(page, size, sort));

        GenericDataDto response = new GenericDataDto();
        response.setDataList(pageResult.getContent().stream()
                .filter(data -> !data.getDeleteFlag())
                .map(data -> mapper.domainToDTO(data, new CycleAvoidingMappingContext()))
                .toList());
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
        response.setTotalRecords(pageResult.getTotalElements());
        response.setPageRecords(pageResult.getNumberOfElements());
        response.setCurrentPageNumber(pageResult.getNumber() + 1);
        response.setTotalPages(pageResult.getTotalPages());
        return response;
    }
}
