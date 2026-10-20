package com.protogsm.gameservercore.dto;

import com.protogsm.architecture.dto.IdentifiableDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GameServerDto extends IdentifiableDto {

	private String idGameServer;
	
	@NotNull(message = "AppID is required")
	@Min(value = 1, message = "AppID must be greater than 0")
	private Integer appID;
    
	@NotBlank(message = "localName is required")
    private String localName;
    
    private String serverName;
    
    private String description;
    
    private String startScript;
    
    private String gameName;
    
    private String status;
    
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getId() {
		return getIdGameServer();
	}
}
