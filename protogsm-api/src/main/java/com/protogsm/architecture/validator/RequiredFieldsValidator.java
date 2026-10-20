package com.protogsm.architecture.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequiredFieldsValidator {
	
	private Map<String, Object> mapFields = new HashMap<>();
	
	public void addRequiredField(String field, Object value) {
		mapFields.put(field, value);
	}
	
	public void validate() {
		Set<String> fields = mapFields.keySet();
		
		for (String field : fields) {
			Object value = mapFields.get(field);
			
			if (value == null) {
				throw new RuntimeException("O Campo " + field + " é obrigatório.");
			}
		}
	}
}
