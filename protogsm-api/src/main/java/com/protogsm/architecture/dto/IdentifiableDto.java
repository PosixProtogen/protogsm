package com.protogsm.architecture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class IdentifiableDto {
	
	abstract public String getId();
	
	@JsonProperty("id")
	public String getJsonId() {
		return getId();
	}
}
