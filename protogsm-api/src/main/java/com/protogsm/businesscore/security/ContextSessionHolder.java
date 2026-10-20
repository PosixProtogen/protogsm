package com.protogsm.businesscore.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class ContextSessionHolder {

	private ContextSessionHolder() {
	}

	public static ContextSession current() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof ContextSession session) {
			return session;
		}
		throw new IllegalStateException("No authenticated user in the current session.");
	}
}
