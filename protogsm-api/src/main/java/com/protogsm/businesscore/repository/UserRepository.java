package com.protogsm.businesscore.repository;

import java.util.Optional;

import com.protogsm.architecture.repository.GenericRepository;
import com.protogsm.businesscore.model.User;

public interface UserRepository extends GenericRepository<User> {
	
//	@EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.LOAD)
	Optional<User> findByUsername(String username);
	
	Optional<User> findBySuperUserTrue();
	
}
