package com.protogsm.architecture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.protogsm.architecture.interfaces.ModulePrefixProvider;

@Configuration
public class CoreModuleConfig implements ModulePrefixProvider {
	
	@Bean(name = "coreModuleMapping")
	@Override
	public ModulePrefixMapping getMapping() {
		return new ModulePrefixMapping(getClass(), "/core/api/v1");
	}
}
