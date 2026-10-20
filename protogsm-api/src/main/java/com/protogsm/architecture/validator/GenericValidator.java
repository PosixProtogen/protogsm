package com.protogsm.architecture.validator;

import com.protogsm.architecture.model.AbstractEntity;

public abstract class GenericValidator<E extends AbstractEntity> {
	
	public RequiredFieldsValidator validateRequiredFields(E entity) {
		return null;
	}

	public void validateInsert(E entity) {
		
	}
	
	public void validateUpdate(E entity) {
		
	}
	
	public void validateDelete(E entity) {
		
	}
}
