package com.protogsm.gameservercore.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SteamCmdWebSocketPublisher {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	public void publishOutput(String idGameServer, String output) {

        messagingTemplate.convertAndSend(
            "/topic/steamcmd/" + idGameServer,
            output
        );
    }

}
