package com.protogsm.gameservercore.service.impl.eventmanager;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.protogsm.gameservercore.model.GameServer;
import com.protogsm.gameservercore.model.enums.ServerStatus;
import com.protogsm.gameservercore.repository.GameServerRepository;
import com.protogsm.gameservercore.service.impl.eventmanager.events.GameServerStoppedEvent;
import com.protogsm.gameservercore.service.impl.eventmanager.events.GameServerUpdatingEvent;
import com.protogsm.gameservercore.service.impl.eventmanager.events.GameServerWaitingEvent;
import com.protogsm.gameservercore.service.impl.eventmanager.events.SteamCmdConsoleOutputEvent;
import com.protogsm.gameservercore.ws.SteamCmdWebSocketPublisher;

@Service
public class GameServerEventManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameServerEventManager.class);
	
	@Autowired
	protected GameServerRepository gameServerRepository;
	
	@Autowired
	protected SteamCmdWebSocketPublisher steamCmdWebSocketPublisher;
	
	@Async
	@EventListener
    public void onSteamCmdConsoleOutput(SteamCmdConsoleOutputEvent steamCmdConsoleOutputEvent) {
		String output = steamCmdConsoleOutputEvent.getOutput();
		String idGameServer = steamCmdConsoleOutputEvent.getIdGameServer();
		
		String process = steamCmdConsoleOutputEvent.getProcess();
		Double percent = steamCmdConsoleOutputEvent.getPercent();
		
//		LOGGER.info("Updating >> GameServer: "+ idGameServer + " -> " + output);
		
        LOGGER.info("Updating >> GameServer: "+ idGameServer + " -> " + process + ":" + percent + "%");
        
        steamCmdWebSocketPublisher.publishOutput(idGameServer, output);
    }
	
	@Async
	@EventListener
	public void onGameServerWaiting(GameServerWaitingEvent gameServerWaitingEvent) {
		String idGameServer = gameServerWaitingEvent.idGameServer();
		
		updateGameServerStatus(idGameServer, ServerStatus.WAITING);
	}
	
	@Async
	@EventListener
	public void onGameServerUpdating(GameServerUpdatingEvent gameServerUpdatingEvent) {
		String idGameServer = gameServerUpdatingEvent.idGameServer();
		
		updateGameServerStatus(idGameServer, ServerStatus.UPDATING);
	}
	
	@Async
	@EventListener
	public void onGameServerStopped(GameServerStoppedEvent gameServerStoppedEvent) {
		String idGameServer = gameServerStoppedEvent.idGameServer();
		
		updateGameServerStatus(idGameServer, ServerStatus.STOPPED);
	}
	
	private synchronized void updateGameServerStatus(String idGameServer, ServerStatus status) {
		Optional<GameServer> optionalGameServer = gameServerRepository.findById(idGameServer);
		
		GameServer gameServer = optionalGameServer.get();
		gameServer.setStatus(status);
		
		gameServerRepository.save(gameServer);
	}
}
