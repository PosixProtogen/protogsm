package com.protogsm.businesscore.model;

import com.protogsm.architecture.model.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ManagerSettings extends AbstractEntity {

	@Id
	private String idManagerSettings;
	
	private String localLibrary;

	public String getIdManagerSettings() {
		return idManagerSettings;
	}

	public void setIdManagerSettings(String idManagerSettings) {
		this.idManagerSettings = idManagerSettings;
	}

	public String getLocalLibrary() {
		return localLibrary;
	}

	public void setLocalLibrary(String localLibrary) {
		this.localLibrary = localLibrary;
	}

	@Override
	public String getId() {
		return getIdManagerSettings();
	}

	@Override
	public void setId(String id) {
		setIdManagerSettings(id);
	}
}
