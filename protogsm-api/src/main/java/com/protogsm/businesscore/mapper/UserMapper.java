package com.protogsm.businesscore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.protogsm.architecture.mapper.BaseMapStruct;
import com.protogsm.businesscore.dto.UserDto;
import com.protogsm.businesscore.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapStruct<User, UserDto> {

	@Mapping(target = "password", ignore = true)
	@Override
	User toEntity(UserDto dto);
	
}
