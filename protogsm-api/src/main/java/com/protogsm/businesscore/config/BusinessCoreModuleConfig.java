package com.protogsm.businesscore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.protogsm.architecture.config.ModulePrefixMapping;
import com.protogsm.architecture.interfaces.ModulePrefixProvider;

@Configuration
public class BusinessCoreModuleConfig implements ModulePrefixProvider {

	@Bean(name = "businessCoreModuleMapping")
	@Override
	public ModulePrefixMapping getMapping() {
		return new ModulePrefixMapping(getClass(), "/businesscore/api/v1");
	}
}
