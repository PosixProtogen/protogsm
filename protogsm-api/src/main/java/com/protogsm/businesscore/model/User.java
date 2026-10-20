package com.protogsm.businesscore.model;

import java.util.Date;

import com.protogsm.architecture.annotations.Loggable;
import com.protogsm.architecture.annotations.NotAudited;
import com.protogsm.architecture.model.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Loggable
@Entity
public class User extends AbstractEntity {
	
	@Id
	@Column(unique = true, nullable = false)
	private String idUser;
	
	private String username;

	@NotAudited
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

	@Override
	public void setId(String id) {
		setIdUser(id);
	}
}
