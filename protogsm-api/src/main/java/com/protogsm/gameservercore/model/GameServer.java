package com.protogsm.gameservercore.model;

import com.protogsm.architecture.model.AbstractEntity;
import com.protogsm.gameservercore.model.enums.ServerStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class GameServer extends AbstractEntity {

	@Id
	@Column(unique = true, nullable = false)
	private String idGameServer;
	
	private Integer appID;
    
    private String localName;
    
    private String serverName;
    
    private String description;
    
    private String startScript;
    
    private String gameName;
    
    @Enumerated(EnumType.STRING)
    private ServerStatus status;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public ServerStatus getStatus() {
		return status;
	}

	public void setStatus(ServerStatus status) {
		this.status = status;
	}

	@Override
	public String getId() {
		return getIdGameServer();
	}

	@Override
	public void setId(String id) {
		setIdGameServer(id);
	}
}
