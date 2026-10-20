package com.protogsm.businesscore.service;

import com.protogsm.architecture.service.CrudService;
import com.protogsm.businesscore.model.ManagerSettings;

public interface ManagerSettingsService extends CrudService<ManagerSettings> {

	String getLocalLibrary();

}
