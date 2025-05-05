package com.wipro.usermanagement.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.usermanagement.dto.AuthenticationRequest;
import com.wipro.usermanagement.dto.AuthenticationResponse;
import com.wipro.usermanagement.dto.RegisterRequest;
import com.wipro.usermanagement.model.User;
import com.wipro.usermanagement.repository.UserRepository;
import com.wipro.usermanagement.util.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	
	
	
	public AuthenticationResponse register(RegisterRequest request) {
		if(userRepository.existsByUserName(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Username or email already exists");
			
		}
		
		User user = User.builder().username(request.getUsername()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).name(request.getName()).address(request.getAddress()).role(request.getRole()).build();
		
		userRepository.save(user);
		String token = jwtService.generateToken(user.getUsername());
		return new AuthenticationResponse(token);
	}
	
	public AuthenticationResponse login(AuthenticationRequest request) {
		Optional<User> optinalUser	= userRepository.findByUsername(request.getUsername());
		
		if(optinalUser.isEmpty()) {
			throw new RuntimeException("Invalid username or password");
		}
		
		
		User user  = optinalUser.get();
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid username or password");
		}
		
		String token = jwtService.generateToken(user.getUsername());
		
		
		return new AuthenticationResponse(token);
		
		
	}
	
	
	
	
	
	
	
	
	
}

