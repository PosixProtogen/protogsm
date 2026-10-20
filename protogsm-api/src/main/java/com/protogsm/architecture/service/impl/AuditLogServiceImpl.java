package com.protogsm.architecture.service.impl;

import com.protogsm.architecture.annotations.Loggable;
import com.protogsm.architecture.annotations.NotAudited;
import com.protogsm.architecture.model.AbstractEntity;
import com.protogsm.architecture.model.AuditLog;
import com.protogsm.architecture.model.AuditLogField;
import com.protogsm.architecture.model.enums.AuditAction;
import com.protogsm.architecture.repository.AuditLogRepository;
import com.protogsm.architecture.service.AuditLogService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Transient;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuditLogServiceImpl<E extends AbstractEntity> implements AuditLogService<E> {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void record(E entity, AuditAction action) {
        if (AnnotationUtils.findAnnotation(Hibernate.getClass(entity), Loggable.class) == null
                || entity.getId() == null
                || entity.getId().isBlank()) {
            return;
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setOccurredAt(LocalDateTime.now());
        auditLog.setUsername(currentUsername());
        auditLog.setAction(action);
        auditLog.setClassName(Hibernate.getClass(entity).getSimpleName());
        auditLog.setEntityId(entity.getId());

        E stored = action == AuditAction.UPDATE ? storedState(entity) : null;

        for (Field field : persistentFields(Hibernate.getClass(entity))) {
            String newValue = action == AuditAction.DELETE ? null : fieldText(field, entity);
            String oldValue = switch (action) {
                case CREATE -> null;
                case DELETE -> fieldText(field, entity);
                case UPDATE -> stored == null ? null : fieldText(field, stored);
            };

            if (action == AuditAction.UPDATE && Objects.equals(oldValue, newValue)) {
                continue;
            }
            if (oldValue == null && newValue == null) {
                continue;
            }

            AuditLogField auditLogField = new AuditLogField();
            auditLogField.setField(field.getName());
            auditLogField.setOldValue(oldValue);
            auditLogField.setNewValue(newValue);
            auditLog.addField(auditLogField);
        }

        if (action == AuditAction.UPDATE && auditLog.getAuditLogFieldSet().isEmpty()) {
            return;
        }
        auditLogRepository.save(auditLog);
    }

    @SuppressWarnings("unchecked")
    private E storedState(E entity) {
        Class<E> type = (Class<E>) Hibernate.getClass(entity);
        return entityManager.find(type, entity.getId());
    }

    private List<Field> persistentFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != AbstractEntity.class && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                if (field.getAnnotation(Transient.class) != null
                        || field.getAnnotation(Id.class) != null
                        || field.getAnnotation(NotAudited.class) != null) {
                    continue;
                }

                if (Iterable.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                field.setAccessible(true);
                fields.add(field);
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    private String fieldText(Field field, E entity) {
        try {
            Object value = field.get(entity);
            if (value == null) {
                return null;
            }

            if (value instanceof Enum<?> enumValue) {
                return enumValue.name();
            }

            if (value instanceof AbstractEntity abstractEntity) {
                return abstractEntity.getCustomDescription();
            }
            return String.valueOf(value);

        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Could not read field " + field.getName(), exception);
        }
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {
            return "anonymous";
        }
        return authentication.getName();
    }
}
