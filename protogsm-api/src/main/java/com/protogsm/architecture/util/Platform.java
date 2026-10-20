package com.protogsm.architecture.util;

public class Platform {
	
	public static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().contains("linux");
	}
	
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
