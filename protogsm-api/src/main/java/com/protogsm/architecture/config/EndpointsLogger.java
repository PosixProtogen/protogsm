package com.protogsm.architecture.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointsLogger implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(EndpointsLogger.class);

	private final RequestMappingHandlerMapping handlerMapping;

	public EndpointsLogger(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	@Override
	public void run(String... args) {
		LOGGER.info("Endpoints:");

		handlerMapping.getHandlerMethods().forEach((info, method) -> {
			String paths = Optional.ofNullable(info.getPatternValues()).map(p -> String.join(",", p))
					.orElse("<desconhecido>");

			String httpMethods = Optional.ofNullable(info.getMethodsCondition()).map(mc -> mc.getMethods().toString())
					.orElse("ANY");

			LOGGER.info("[{}] {} → {}.{}()", httpMethods, paths, method.getBeanType().getSimpleName(),
					method.getMethod().getName());
		});
	}
}
