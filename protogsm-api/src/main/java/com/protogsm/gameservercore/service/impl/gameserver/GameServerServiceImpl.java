package com.protogsm.gameservercore.service.impl.gameserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protogsm.architecture.service.impl.CrudServiceImpl;
import com.protogsm.gameservercore.model.GameServer;
import com.protogsm.gameservercore.model.enums.ServerStatus;
import com.protogsm.gameservercore.service.GameServerService;
import com.protogsm.gameservercore.service.SteamCmdService;

import jakarta.annotation.PostConstruct;

@Service
public class GameServerServiceImpl extends CrudServiceImpl<GameServer> implements GameServerService {
	
	@Autowired
	protected SteamCmdService steamCmdService;
	
	public GameServerServiceImpl() {
		onPostCreate(this::updateGameServer);
	}
	
	@PostConstruct
	protected void init() {
		List<GameServer> gameServerList = getRepository().findAll();
		
		for (GameServer gameServer : gameServerList) {
			gameServer.setStatus(ServerStatus.STOPPED);
			
			getRepository().save(gameServer);
		}
	}
	
	@Override
	public void updateGameServer(String idGameServer) {
		GameServer gameServer = findById(idGameServer);
		
		updateGameServer(gameServer);
	}
	
	private void updateGameServer(GameServer entity) {
		steamCmdService.updateServer(entity);
	}
}
