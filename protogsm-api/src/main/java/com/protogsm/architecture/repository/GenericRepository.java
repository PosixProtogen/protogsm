package com.protogsm.architecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.protogsm.architecture.model.AbstractEntity;

@NoRepositoryBean
public interface GenericRepository<E extends AbstractEntity> extends JpaRepository<E, String>, JpaSpecificationExecutor<E> {

}
