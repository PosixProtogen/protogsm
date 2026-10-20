package com.protogsm.architecture.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.protogsm.architecture.dto.IdentifiableDto;
import com.protogsm.architecture.mapper.BaseMapStruct;
import com.protogsm.architecture.model.AbstractEntity;
import com.protogsm.architecture.service.CrudService;

import jakarta.validation.Valid;

public abstract class CrudController<E extends AbstractEntity, DTO extends IdentifiableDto, S extends CrudService<E>> {

    @Autowired
    protected S crudService;

    @Autowired
    protected BaseMapStruct<E, DTO> mapper;

    @PostMapping
    public DTO create(@RequestBody @Valid DTO dto) {
        E entity = convertDtoToEntity(dto);

        entity = crudService.create(entity);

        return convertEntityToDto(entity);
    }

    @PutMapping
    public DTO update(@RequestBody @Valid DTO dto) {
        E entity = convertDtoToEntity(dto);

        entity = crudService.update(entity);

        return convertEntityToDto(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        crudService.delete(id);
    }

    @GetMapping("/{id}")
    public DTO getById(@PathVariable String id) {
        E entity = crudService.findById(id);

        return convertEntityToDto(entity);
    }

    @GetMapping
    public Page<DTO> list(Pageable pageable, @RequestParam(required = false) Map<String, String> filters) {
        Page<E> page = crudService.findPaged(filters, pageable);

        return page.map(this::convertEntityToDto);
    }

    protected S getService() {
        return crudService;
    }

    protected E convertDtoToEntity(DTO dto) {
        return mapper.toEntity(dto);
    }

    protected DTO convertEntityToDto(E entity) {
        return mapper.toDto(entity);
    }
}
 