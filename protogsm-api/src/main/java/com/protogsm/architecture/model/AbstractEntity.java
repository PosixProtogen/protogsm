package com.protogsm.architecture.model;

import com.github.f4b6a3.ulid.UlidCreator;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

@MappedSuperclass
public abstract class AbstractEntity {

	abstract public String getId();
	
	abstract public void setId(String id);

	@PrePersist
	public void generateIdIfNeeded() {
		if (getId() == null || getId().isBlank()) {
			setId(UlidCreator.getMonotonicUlid().toString());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		
		if (obj instanceof AbstractEntity entity) {
			
			if (entity.getId() == null) {
				return false;
			}
			
			return entity.getId().equals(getId());
		}
		return false;
	}

	public String getCustomDescription() {
		return getId();
	}
}
