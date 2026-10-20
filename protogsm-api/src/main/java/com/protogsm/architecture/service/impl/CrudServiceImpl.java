package com.protogsm.architecture.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.protogsm.architecture.model.AbstractEntity;
import com.protogsm.architecture.model.enums.AuditAction;
import com.protogsm.architecture.repository.GenericRepository;
import com.protogsm.architecture.search.FieldSearch;
import com.protogsm.architecture.service.AuditLogService;
import com.protogsm.architecture.service.CrudService;
import com.protogsm.architecture.validator.GenericValidator;
import com.protogsm.architecture.validator.RequiredFieldsValidator;

import jakarta.transaction.Transactional;

@Transactional
public class CrudServiceImpl<E extends AbstractEntity> implements CrudService<E> {
	
	@Autowired
	protected GenericRepository<E> repository;

	@Autowired
	protected AuditLogService<E> auditLogService;

	@Autowired(required = false)
	protected GenericValidator<E> validator;
	
	private List<Consumer<E>> consumersPrepareCreate = new ArrayList<>();
	
	private List<Consumer<E>> consumersPostCreate = new ArrayList<>();
	
	private List<Consumer<E>> consumersPrepareUpdate = new ArrayList<>();
	
	private List<Consumer<E>> consumersPostUpdate = new ArrayList<>();
	
	private List<Consumer<E>> consumersPrepareDelete = new ArrayList<>();
	
	private List<Consumer<E>> consumersPostDelete = new ArrayList<>();
	
	@Override
	public E create(E entity) {
		for (Consumer<E> consumerPrepareCreate : consumersPrepareCreate) {
			consumerPrepareCreate.accept(entity);
		}
		
		if (validator != null) {
			validateRequiredFields(entity);
			validator.validateInsert(entity);
		}
		entity = repository.save(entity);

		auditLogService.record(entity, AuditAction.CREATE);
		
		for (Consumer<E> consumerPostCreate : consumersPostCreate) {
			consumerPostCreate.accept(entity);
		}
		
		return entity;
	}

	@Override
	public E update(E entity) {
		for (Consumer<E> consumerPrepareUpdate : consumersPrepareUpdate) {
			consumerPrepareUpdate.accept(entity);
		}
		
		if (validator != null) {
			validateRequiredFields(entity);
			validator.validateUpdate(entity);
		}

		auditLogService.record(entity, AuditAction.UPDATE);

		entity = repository.save(entity);
		
		for (Consumer<E> consumerPostUpdate : consumersPostUpdate) {
			consumerPostUpdate.accept(entity);
		}
		
		return entity;
	}

	@Override
	public void delete(String id) {
		Optional<E> optional = repository.findById(id);
		
		if (!optional.isPresent()) {
			throw new RuntimeException("Entity not found.\n id: " + id);
		}
		
		E entity = optional.get();
		
		for (Consumer<E> consumerPrepareDelete : consumersPrepareDelete) {
			consumerPrepareDelete.accept(entity);
		}
		
		if (validator != null) {
			validator.validateDelete(entity);
		}

		auditLogService.record(entity, AuditAction.DELETE);

		repository.delete(entity);
		
		for (Consumer<E> consumerPostDelete : consumersPostDelete) {
			consumerPostDelete.accept(entity);
		}
	}
	
	@Override
	public E findById(String id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
	}

	@Override
	public Page<E> findPaged(Pageable pageable) {
		return findPaged(Map.of(), pageable);
	}

	@Override
	public Page<E> findPaged(Map<String, String> filters, Pageable pageable) {
		Specification<E> specification = FieldSearch.toSpecification(entityClass(), filters);
		return repository.findAll(specification, pageable);
	}

	@SuppressWarnings("unchecked")
	private Class<E> entityClass() {
		Class<?> resolved = ResolvableType.forClass(getClass()).as(CrudServiceImpl.class).getGeneric(0).resolve();
		if (resolved == null) {
			throw new IllegalStateException("Could not resolve the entity class for " + getClass().getName());
		}
		return (Class<E>) resolved;
	}
	
	private void validateRequiredFields(E entity) {
		RequiredFieldsValidator validateRequiredFields = validator.validateRequiredFields(entity);
		
		if (validateRequiredFields != null) {
			validateRequiredFields.validate();
		}
	}

	protected void onPrepareCreate(Consumer<E> consumer) {
		consumersPrepareCreate.add(consumer);
	}
	
	protected void onPostCreate(Consumer<E> consumer) {
		consumersPostCreate.add(consumer);
	}
	
	protected void onPrepareUpdate(Consumer<E> consumer) {
		consumersPrepareUpdate.add(consumer);
	}
	
	protected void onPostUpdate(Consumer<E> consumer) {
		consumersPostUpdate.add(consumer);
	}
	
	protected void onPrepareDelete(Consumer<E> consumer) {
		consumersPrepareDelete.add(consumer);
	}
	
	protected void onPostDelete(Consumer<E> consumer) {
		consumersPostDelete.add(consumer);
	}

	public GenericRepository<E> getRepository() {
		return repository;
	}
}
