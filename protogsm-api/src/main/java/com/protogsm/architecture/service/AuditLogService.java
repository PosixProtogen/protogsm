package com.protogsm.architecture.service;

import com.protogsm.architecture.model.AbstractEntity;
import com.protogsm.architecture.model.enums.AuditAction;

public interface AuditLogService<E extends AbstractEntity> {

    void record(E entity, AuditAction action);

}
