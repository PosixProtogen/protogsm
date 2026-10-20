package com.protogsm.gameservercore.service.impl.steamcmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.protogsm.businesscore.service.ManagerSettingsService;
import com.protogsm.gameservercore.beans.GameServerBean;
import com.protogsm.gameservercore.mapper.GameServerBeanMapper;
import com.protogsm.gameservercore.model.GameServer;
import com.protogsm.gameservercore.service.SteamCmdService;

@Service
public class SteamCmdServiceImpl implements SteamCmdService {
	
	@Autowired
	protected GameServerBeanMapper gameServerBeanMapper;
	
	@Autowired
	protected ManagerSettingsService managerSettingsService;
	
	@Autowired
	protected ApplicationEventPublisher publisher;
	
	private SteamCmdWorker steamCmdWorker;
	
	@Override
	public void updateServer(GameServer gameServer) {
		initSteamCmdWorker();
		
		GameServerBean gameServerBean = gameServerBeanMapper.toDto(gameServer);
		
		steamCmdWorker.addToQueue(gameServerBean);
	}

	private synchronized void initSteamCmdWorker() {
		if (steamCmdWorker == null) {
			String localLibrary = managerSettingsService.getLocalLibrary();
			
			steamCmdWorker = new SteamCmdWorker(localLibrary, publisher);
			
			steamCmdWorker.start();
		}
	}
}
