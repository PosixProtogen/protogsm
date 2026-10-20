package com.protogsm.businesscore.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.protogsm.architecture.dto.IdentifiableDto;

import jakarta.validation.constraints.NotBlank;

public class UserDto extends IdentifiableDto {
	
	private String idUser;
	
	@NotBlank(message = "Username é obrigatório")
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
    
    private boolean superUser;
    
    private Date creationDate;

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String getId() {
		return getIdUser();
	}
}
