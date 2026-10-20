package com.protogsm.gameservercore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.protogsm.architecture.config.ModulePrefixMapping;
import com.protogsm.architecture.interfaces.ModulePrefixProvider;

@Configuration
public class GameServerCoreModuleConfig implements ModulePrefixProvider {
	
	@Bean(name = "gameServerCoreModuleMapping")
	@Override
	public ModulePrefixMapping getMapping() {
		return new ModulePrefixMapping(getClass(), "/gameservercore/api/v1");
	}

}
