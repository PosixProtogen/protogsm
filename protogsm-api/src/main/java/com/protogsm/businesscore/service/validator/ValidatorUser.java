package com.protogsm.businesscore.service.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.protogsm.architecture.validator.GenericValidator;
import com.protogsm.businesscore.model.User;
import com.protogsm.businesscore.repository.UserRepository;

@Component
public class ValidatorUser extends GenericValidator<User> {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void validateInsert(User user) {
		if (user.isSuperUser()) {
			Optional<User> superUserOptional = userRepository.findBySuperUserTrue();
			
			if (superUserOptional.isPresent()) {
				throw new RuntimeException(
						"An administrator account already exists. Sign in with that account instead of creating another one.");
			}
		}
	}
}
