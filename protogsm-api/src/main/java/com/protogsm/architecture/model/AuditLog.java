package com.protogsm.architecture.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.protogsm.architecture.model.enums.AuditAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class AuditLog extends AbstractEntity {

	@Id
	private String idAuditLog;

	@Column(nullable = false)
	private LocalDateTime occurredAt;

	@Column(nullable = false)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AuditAction action;

	@Column(nullable = false)
	private String className;

	@Column(nullable = false)
	private String entityId;

	@OneToMany(mappedBy = "auditLog", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuditLogField> auditLogFieldSet = new HashSet<>();

	public String getIdAuditLog() {
		return idAuditLog;
	}

	public void setIdAuditLog(String idAuditLog) {
		this.idAuditLog = idAuditLog;
	}

	public LocalDateTime getOccurredAt() {
		return occurredAt;
	}

	public void setOccurredAt(LocalDateTime occurredAt) {
		this.occurredAt = occurredAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AuditAction getAction() {
		return action;
	}

	public void setAction(AuditAction action) {
		this.action = action;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Set<AuditLogField> getAuditLogFieldSet() {
		return auditLogFieldSet;
	}

	public void addField(AuditLogField field) {
		auditLogFieldSet.add(field);
		field.setAuditLog(this);
	}

	@Override
	public String getId() {
		return getIdAuditLog();
	}

	@Override
	public void setId(String id) {
		setIdAuditLog(id);
	}
}
