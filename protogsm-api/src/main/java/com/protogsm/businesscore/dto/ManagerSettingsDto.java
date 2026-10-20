package com.protogsm.businesscore.dto;

import com.protogsm.architecture.dto.IdentifiableDto;

import jakarta.validation.constraints.NotBlank;

public class ManagerSettingsDto extends IdentifiableDto {

	private String idManagerSettings;
	
	@NotBlank(message = "Local Library is null")
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
}
