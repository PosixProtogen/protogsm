package com.protogsm.gameservercore.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protogsm.architecture.controller.CrudController;
import com.protogsm.gameservercore.dto.GameServerDto;
import com.protogsm.gameservercore.model.GameServer;
import com.protogsm.gameservercore.service.GameServerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/game-server")
@RequiredArgsConstructor
public class GameServerController extends CrudController<GameServer, GameServerDto, GameServerService> {
	
	@PostMapping("/{id}/update")
	public void updateGameServer(@PathVariable("id") String idGameServer) {
		getService().updateGameServer(idGameServer);
	}
}
