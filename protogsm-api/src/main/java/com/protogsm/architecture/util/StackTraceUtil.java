package com.protogsm.architecture.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {
	
	public static String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
