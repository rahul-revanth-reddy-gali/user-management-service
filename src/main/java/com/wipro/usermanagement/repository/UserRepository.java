package com.wipro.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.usermanagement.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

	
	Optional<User> findByUsername(String username);
	
	boolean existsByUserName(String username);
	
	boolean existsByEmail(String email);
	
	
	
}
