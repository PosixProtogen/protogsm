package com.protogsm.businesscore.service.impl;

import com.protogsm.architecture.service.impl.CrudServiceImpl;
import com.protogsm.businesscore.config.SuperUserProperties;
import com.protogsm.businesscore.model.User;
import com.protogsm.businesscore.repository.UserRepository;
import com.protogsm.businesscore.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl extends CrudServiceImpl<User> implements UserService {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	private SuperUserProperties superUserProperties;

	@Autowired
	private UserRepository userRepository;

	public UserServiceImpl(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		onPrepareCreate(this::encodePassword);
		onPrepareCreate(this::setCreationDate);
	}

	@PostConstruct
	private void createSuperUser() {
		Optional<User> superUserOptional = userRepository.findBySuperUserTrue();

		if (superUserOptional.isEmpty()){
			User user = new User();
			user.setUsername(superUserProperties.getUsername());
			user.setPassword(superUserProperties.getPassword());
			user.setSuperUser(true);

			create(user);
		}
	}

	private void encodePassword(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}
	
	private void setCreationDate(User user) {
		user.setCreationDate(new Date());
	}
}
