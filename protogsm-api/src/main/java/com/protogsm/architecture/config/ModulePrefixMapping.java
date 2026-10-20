package com.protogsm.architecture.config;

public class ModulePrefixMapping {
	
	 private final String packageName;
	 
	 private final String prefix;

	 public ModulePrefixMapping(Class<?> clazz, String prefix) {
		this.packageName = clazz.getPackageName().substring(0, clazz.getPackageName().lastIndexOf('.')) + ".controller";
		this.prefix = prefix;
	 }

	 public String getPackageName() {
		 return packageName;
	 }

	 public String getPrefix() {
		 return prefix;
	 }
}
