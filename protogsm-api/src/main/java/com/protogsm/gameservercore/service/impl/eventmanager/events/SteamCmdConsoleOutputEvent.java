package com.protogsm.gameservercore.service.impl.eventmanager.events;

public class SteamCmdConsoleOutputEvent {
	
	private String idGameServer;
	
	private String output;

	public String getIdGameServer() {
		return idGameServer;
	}

	public void setIdGameServer(String idGameServer) {
		this.idGameServer = idGameServer;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	public String getProcess() {
		if (output == null) {
			return "";
		}
		
		if (output.contains("verifying") 
				|| output.contains("downloading") 
				|| output.contains("reconfiguring")
				|| output.contains("stagging")) {

			String[] splitOut = output.split(":");
			String[] statusStringSplit = splitOut[0].split(" ");

			return statusStringSplit[4].replace(",", "");
		}
		return "";
	}

	public Double getPercent() {
		if (output == null) {
			return Double.NaN;
		}
		
		if (output.contains("verifying") 
				|| output.contains("downloading") 
				|| output.contains("reconfiguring")
				|| output.contains("stagging")) {

			String[] splitOut = output.split(":");
			String[] pctStringSplit = splitOut[1].split(" ");

			return Double.parseDouble(pctStringSplit[1]);
		}
		return Double.NaN;
	}
}
