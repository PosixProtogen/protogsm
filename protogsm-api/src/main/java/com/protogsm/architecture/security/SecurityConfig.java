package com.protogsm.architecture.security;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.protogsm.architecture.annotations.PublicEndpoint;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, RequestMappingHandlerMapping handlerMapping,
			SecurityContextRepository securityContextRepository) throws Exception {
		http.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
						.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.securityContext(context -> context.securityContextRepository(securityContextRepository))
				.exceptionHandling(exceptions -> exceptions
						.authenticationEntryPoint((request, response, authException) -> response
								.sendError(HttpServletResponse.SC_UNAUTHORIZED))
						.accessDeniedHandler((request, response, accessDeniedException) -> response
								.sendError(HttpServletResponse.SC_FORBIDDEN)))
				.authorizeHttpRequests(auth -> {
					auth.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll();
					permitPublicEndpoints(auth, handlerMapping);
					auth.anyRequest().authenticated();
				});

		return http.build();
	}

	private void permitPublicEndpoints(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth,
			RequestMappingHandlerMapping handlerMapping) {
		handlerMapping.getHandlerMethods().forEach((mapping, handlerMethod) -> {
			if (!isPublic(handlerMethod)) {
				return;
			}
			Set<String> patterns = mapping.getPatternValues();
			if (patterns.isEmpty()) {
				return;
			}
			String[] paths = patterns.toArray(String[]::new);
			Set<RequestMethod> methods = mapping.getMethodsCondition().getMethods();
			if (methods.isEmpty()) {
				auth.requestMatchers(paths).permitAll();
				return;
			}
			for (RequestMethod method : methods) {
				auth.requestMatchers(HttpMethod.valueOf(method.name()), paths).permitAll();
			}
		});
	}

	private boolean isPublic(HandlerMethod handlerMethod) {
		return handlerMethod.hasMethodAnnotation(PublicEndpoint.class)
				|| AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), PublicEndpoint.class);
	}

	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
