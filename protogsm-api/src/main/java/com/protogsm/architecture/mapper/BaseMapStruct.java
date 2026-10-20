package com.protogsm.architecture.mapper;

import java.util.List;

public interface BaseMapStruct<E, DTO> {
	
	DTO toDto(E entity);

	E toEntity(DTO dto);
	
	List<DTO> toDtoList(List<E> entities);
	
	List<E> toEntityList(List<DTO> dtos);
	
}
