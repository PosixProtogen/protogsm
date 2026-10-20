package com.protogsm.gameservercore.mapper;


import org.mapstruct.Mapper;

import com.protogsm.architecture.mapper.BaseMapStruct;
import com.protogsm.gameservercore.dto.GameServerDto;
import com.protogsm.gameservercore.model.GameServer;

@Mapper(componentModel = "spring")
public interface GameServerMapper extends BaseMapStruct<GameServer, GameServerDto> {

}
