package com.protogsm.architecture.config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import jakarta.annotation.PostConstruct;

@Configuration
public class ApiPrefixConfig {

	@Autowired
	private List<ModulePrefixMapping> prefixMappings;
	
	private Map<String, String> prefixMap;
	
	@PostConstruct
	public void init() {
		prefixMap = prefixMappings.stream()
				.collect(Collectors.toMap(m -> m.getPackageName(), ModulePrefixMapping::getPrefix));
	}
	
    @Bean
	public WebMvcRegistrations mvcRegistrations() {
		return new WebMvcRegistrations() {
			@Override
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				return new RequestMappingHandlerMapping() {
					@Override
					protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
						ApplicationContext applicationContext = getApplicationContext();
						
						String moduleNameController = getModuleNameController(handler, applicationContext);
						
						String prefix = prefixMap.get(moduleNameController);
						
						if (prefix != null) {
						    mapping = RequestMappingInfo.paths(prefix).build().combine(mapping);
						}
 						super.registerHandlerMethod(handler, method, mapping);
 					}
				};
			}
		};
	}
    
    private String getModuleNameController(Object handler, ApplicationContext applicationContext) {
		Class<?> beanType;

		if (handler instanceof String) {
		    beanType = applicationContext.getType((String) handler);
		} else {
		    beanType = handler.getClass();
		}
		return beanType.getPackageName();
	}
}
