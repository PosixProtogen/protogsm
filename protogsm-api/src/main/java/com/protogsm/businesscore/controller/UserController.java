package com.protogsm.businesscore.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protogsm.architecture.annotations.PublicEndpoint;
import com.protogsm.architecture.controller.CrudController;
import com.protogsm.businesscore.config.SuperUserProperties;
import com.protogsm.businesscore.dto.UserDto;
import com.protogsm.businesscore.model.User;
import com.protogsm.businesscore.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends CrudController<User, UserDto, UserService> {

	@GetMapping("/me")
	public String me(Principal principal) {
		return "Usuário logado: " + principal.getName() + " Thread" + Thread.currentThread();
	}
}
