package com.protogsm.businesscore.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protogsm.architecture.annotations.PublicEndpoint;
import com.protogsm.businesscore.dto.LoginDto;
import com.protogsm.businesscore.security.ContextSession;
import com.protogsm.businesscore.security.UserDetailsImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;

	private final SecurityContextRepository securityContextRepository;

	@PublicEndpoint
	@GetMapping("/csrf")
	public ResponseEntity<Void> csrf() {
		return ResponseEntity.noContent().build();
	}

	@PublicEndpoint
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto login, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

			UserDetailsImpl details = (UserDetailsImpl) auth.getPrincipal();

			ContextSession contextSession = new ContextSession(details.getId(), details.getUsername(),
					auth.getAuthorities());

			Authentication loggedIn = new UsernamePasswordAuthenticationToken(contextSession, null,
					contextSession.getAuthorities());

			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			securityContext.setAuthentication(loggedIn);
			SecurityContextHolder.setContext(securityContext);

			if (request.getSession(false) != null) {
				request.changeSessionId();
			}
			securityContextRepository.saveContext(securityContext, request, response);

			return ResponseEntity.ok(Map.of("authenticated", true));
		} catch (AuthenticationException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextHolder.clearContext();

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		Cookie cookie = new Cookie("JSESSIONID", "");
		String contextPath = request.getContextPath();
		cookie.setPath(contextPath == null || contextPath.isEmpty() ? "/" : contextPath);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return ResponseEntity.noContent().build();
	}
}
