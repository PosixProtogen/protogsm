package com.protogsm.businesscore.repository;

import java.util.Optional;

import com.protogsm.architecture.repository.GenericRepository;
import com.protogsm.businesscore.model.ManagerSettings;

public interface ManagerSettingsRepository extends GenericRepository<ManagerSettings> {

	Optional<ManagerSettings> findFirstBy();
}
