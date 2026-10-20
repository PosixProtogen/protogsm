package com.protogsm.gameservercore.service;

import com.protogsm.architecture.service.CrudService;
import com.protogsm.gameservercore.model.GameServer;

public interface GameServerService extends CrudService<GameServer> {
	
	void updateGameServer(String idGameServer);
	
}
