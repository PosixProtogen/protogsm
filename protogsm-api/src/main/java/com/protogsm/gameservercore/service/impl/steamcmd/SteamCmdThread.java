package com.protogsm.gameservercore.service.impl.steamcmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

import org.springframework.context.ApplicationEventPublisher;

import com.protogsm.gameservercore.beans.GameServerBean;
import com.protogsm.gameservercore.service.impl.eventmanager.events.SteamCmdConsoleOutputEvent;

public class SteamCmdThread extends Thread {

	private Semaphore semaphore;
	
	private Process pty;
	
	private ApplicationEventPublisher publisher;
	
	private GameServerBean gameServerBean;

	public SteamCmdThread(Semaphore semaphore, ApplicationEventPublisher publisher, Process pty, GameServerBean gameServerBean) {
		this.semaphore = semaphore;
		this.publisher = publisher;
		this.pty = pty;
		this.gameServerBean = gameServerBean;
	}

	@Override
	public void run() {
		InputStream stdout = pty.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8), 1);

		try {
			while (true) {
				
				String out = reader.readLine();

				if (out == null) {
					semaphore.release();
					break;
				} else if (out.contains("[1m") || (out.contains("[97m") && !out.contains("Steam>"))) {
					semaphore.release();
					
				} else if (out.contains("code from your Steam Guard")
						|| out.contains("This computer has not been authenticated")) {

					String authCode = "";

//					for (SteamCmdListener l : listeners) {
//						authCode = l.onAuthCode();
//					}

					OutputStream stdin = pty.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

					writer.write(authCode + "\n");
					writer.flush();

				} else if (out.contains("Two-factor code mismatch") || out.contains("Invalid Login Auth Code")) {
//					for (SteamCmdListener l : listeners) {
//						l.onFailedLoginCode();
//					}
				} else if (out.contains("Invalid Password")) {
//					for (SteamCmdListener l : listeners) {
//						l.onInvalidPassword();
//					}
				} else {
					SteamCmdConsoleOutputEvent steamCmdEvent = new SteamCmdConsoleOutputEvent();
					steamCmdEvent.setIdGameServer(gameServerBean.getIdGameServer());
					steamCmdEvent.setOutput(out);
					
					publisher.publishEvent(steamCmdEvent);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
