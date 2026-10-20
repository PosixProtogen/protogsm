package com.protogsm.architecture.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.protogsm.architecture.util.StackTraceUtil;

@ControllerAdvice
public class ValidationExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

		Map<String, Object> mapReturnException = new LinkedHashMap<>();
		mapReturnException.put("exception", ex.getClass().getSimpleName());
		mapReturnException.put("message", message);
		mapReturnException.put("stackTrace", StackTraceUtil.getStackTraceAsString(ex));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapReturnException);
	}

	@ExceptionHandler(InvalidSearchFieldException.class)
	public ResponseEntity<Map<String, String>> handleInvalidSearch(InvalidSearchFieldException ex) {
		Map<String, String> mapReturnException = new LinkedHashMap<>();
		mapReturnException.put("exception", ex.getClass().getSimpleName());
		mapReturnException.put("message", ex.getMessage());
		mapReturnException.put("stackTrace", StackTraceUtil.getStackTraceAsString(ex));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapReturnException);
	}

	@ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handle(Exception ex) {
		Map<String, String> mapReturnException = new LinkedHashMap<>();
		mapReturnException.put("exception", ex.getClass().getSimpleName());
		mapReturnException.put("message", ex.getMessage());
		mapReturnException.put("stackTrace", StackTraceUtil.getStackTraceAsString(ex));
		
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapReturnException);
    }
}
