package com.protogsm.gameservercore.beans;

import java.io.Serializable;

public class GameServerBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String idGameServer;
    
    private Integer appID;
    
    private String localName;
    
    private String serverName;
    
    private String startScript;
    
    private String gameName;

	public String getIdGameServer() {
		return idGameServer;
	}

	public void setIdGameServer(String idGameServer) {
		this.idGameServer = idGameServer;
	}

	public Integer getAppID() {
		return appID;
	}

	public void setAppID(Integer appID) {
		this.appID = appID;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getStartScript() {
		return startScript;
	}

	public void setStartScript(String startScript) {
		this.startScript = startScript;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
