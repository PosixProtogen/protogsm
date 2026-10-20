package com.protogsm.businesscore.service.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.protogsm.architecture.validator.GenericValidator;
import com.protogsm.businesscore.model.ManagerSettings;
import com.protogsm.businesscore.repository.ManagerSettingsRepository;

@Component
public class ValidatorManagerSettings extends GenericValidator<ManagerSettings> {
	
	@Autowired
	protected ManagerSettingsRepository managerSettingsRepository;
	
	@Override
	public void validateInsert(ManagerSettings entity) {
		Optional<ManagerSettings> optionalManagerSettings = managerSettingsRepository.findFirstBy();
		
		if (!optionalManagerSettings.isEmpty()) {
			throw new RuntimeException("Configuration already registered.");
		}
	}

}
