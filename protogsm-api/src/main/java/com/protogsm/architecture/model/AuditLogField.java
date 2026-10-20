package com.protogsm.architecture.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AuditLogField extends AbstractEntity {

	@Id
	private String idAuditLogField;

	@ManyToOne(optional = false)
	@JoinColumn(name = "idAuditLog")
	private AuditLog auditLog;

	@Column(nullable = false)
	private String field;

	@Column(columnDefinition = "text")
	private String oldValue;

	@Column(columnDefinition = "text")
	private String newValue;

	public String getIdAuditLogField() {
		return idAuditLogField;
	}

	public void setIdAuditLogField(String idAuditLogField) {
		this.idAuditLogField = idAuditLogField;
	}

	public AuditLog getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(AuditLog auditLog) {
		this.auditLog = auditLog;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Override
	public String getId() {
		return getIdAuditLogField();
	}

	@Override
	public void setId(String id) {
		setIdAuditLogField(id);
	}
}
