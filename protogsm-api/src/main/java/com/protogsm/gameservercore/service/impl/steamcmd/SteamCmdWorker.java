package com.protogsm.gameservercore.service.impl.steamcmd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;

import com.protogsm.gameservercore.beans.GameServerBean;
import com.protogsm.gameservercore.service.impl.eventmanager.events.GameServerStoppedEvent;
import com.protogsm.gameservercore.service.impl.eventmanager.events.GameServerUpdatingEvent;
import com.protogsm.gameservercore.service.impl.eventmanager.events.GameServerWaitingEvent;

public class SteamCmdWorker extends Thread {
	
	private SteamCmd steamCmd;
	
	private String localLibrary;
	
	private List<GameServerBean> updateList = new ArrayList<>();
	
	private ApplicationEventPublisher publisher;
	
	public SteamCmdWorker(String localLibrary, ApplicationEventPublisher publisher) {
		this.localLibrary = localLibrary;
		this.publisher = publisher;
		
		steamCmd = new SteamCmdBuilder().setLocalDir(localLibrary).addPublisher(publisher).build();
	}

	public synchronized void addToQueue(GameServerBean serverGame) {
    	if (!updateList.contains(serverGame)) {
    		publisher.publishEvent(new GameServerWaitingEvent(serverGame.getIdGameServer()));
    		
            updateList.add(serverGame);
            
            synchronized (this) {
                notify();
            }
    	}
    }

	@Override
    public void run() {
        while (true) {
        	GameServerBean gameServer = getUpdateJob();
        	
        	steamCmd.setGameServer(gameServer);
            steamCmd.start();
            steamCmd.forceInstallDir(getInstallDir(gameServer));
            steamCmd.loginAnonymous();
            steamCmd.appUpdate(gameServer.getAppID(), "validate");
            steamCmd.logout();
            
            completedUpdateJob(gameServer);
        }
    }

	private synchronized GameServerBean getUpdateJob() {
        if (updateList.isEmpty()) {
        	steamCmd.stop();
        	
            try {
                wait();
                
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        GameServerBean serverGame = updateList.get(0); 
        
        publisher.publishEvent(new GameServerUpdatingEvent(serverGame.getIdGameServer()));
        
        return serverGame;
    }
    
    public void completedUpdateJob(GameServerBean serverGame) {
    	publisher.publishEvent(new GameServerStoppedEvent(serverGame.getIdGameServer()));
            
        updateList.remove(serverGame);
    }
    
    private String getInstallDir(GameServerBean gameServer) {
		String installDir = localLibrary + File.separator + gameServer.getLocalName();
		
		File localDir = new File(installDir);

		if (!localDir.exists()) {
		    localDir.mkdir();
		}
		return installDir;
	}
}
