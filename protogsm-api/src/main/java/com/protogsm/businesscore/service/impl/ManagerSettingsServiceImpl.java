package com.protogsm.businesscore.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protogsm.architecture.service.impl.CrudServiceImpl;
import com.protogsm.businesscore.model.ManagerSettings;
import com.protogsm.businesscore.repository.ManagerSettingsRepository;
import com.protogsm.businesscore.service.ManagerSettingsService;

@Service
public class ManagerSettingsServiceImpl extends CrudServiceImpl<ManagerSettings> implements ManagerSettingsService {
	
	@Autowired
	protected ManagerSettingsRepository managerSettingsRepository;
	
	@Override
	public String getLocalLibrary() {
		ManagerSettings managerSettings = getManagerSettings();
		
		String localLibrary = managerSettings.getLocalLibrary();
		
		if (localLibrary == null) {
			throw new RuntimeException("Local Library não cadastrado.");
		}
		
		return localLibrary;
	}

	private ManagerSettings getManagerSettings() {
		Optional<ManagerSettings> optionalManagerSettings = managerSettingsRepository.findFirstBy();
		
		if (optionalManagerSettings.isEmpty()) {
			throw new RuntimeException("Settings não cadastrado.");
		}
		return optionalManagerSettings.get();
	}
}
