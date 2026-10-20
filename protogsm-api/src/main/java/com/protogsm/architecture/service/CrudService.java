package com.protogsm.architecture.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.protogsm.architecture.model.AbstractEntity;

public interface CrudService<E extends AbstractEntity> {
	
	E create(E entity);
	
	E update(E entity);
	
	void delete(String id);
	
	E findById(String id);
	
	Page<E> findPaged(Pageable pageable);

	Page<E> findPaged(Map<String, String> filters, Pageable pageable);
	
}
