package com.protogsm.gameservercore.mapper;

import org.mapstruct.Mapper;

import com.protogsm.architecture.mapper.BaseMapStruct;
import com.protogsm.gameservercore.beans.GameServerBean;
import com.protogsm.gameservercore.model.GameServer;

@Mapper(componentModel = "spring")
public interface GameServerBeanMapper extends BaseMapStruct<GameServer, GameServerBean> {

}
