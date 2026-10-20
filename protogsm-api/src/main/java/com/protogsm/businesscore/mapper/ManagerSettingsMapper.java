package com.protogsm.businesscore.mapper;

import org.mapstruct.Mapper;

import com.protogsm.architecture.mapper.BaseMapStruct;
import com.protogsm.businesscore.dto.ManagerSettingsDto;
import com.protogsm.businesscore.model.ManagerSettings;

@Mapper(componentModel = "spring")
public interface ManagerSettingsMapper extends BaseMapStruct<ManagerSettings, ManagerSettingsDto> {

}
