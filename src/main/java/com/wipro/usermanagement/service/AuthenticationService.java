package com.wipro.usermanagement.service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.usermanagement.dto.AuthenticationRequest;
import com.wipro.usermanagement.dto.AuthenticationResponse;
import com.wipro.usermanagement.dto.RegisterRequest;
import com.wipro.usermanagement.dto.UpdateUserRequest;
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

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setUserType(request.getUserType());
        user.setUserId(UUID.randomUUID().toString());
        user.setLoggedIn(false);; // optional here
        if(request.getUserType() ==1 ) {
        	user.setRole("ADMIN");
        }
        else if (request.getUserType() ==0) {
        	user.setRole("CUSTOMER");
        }

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, "User registered successfully");
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        user.setLoggedIn(true);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, "Login successful");
    }

    public String logout(String token) {
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLoggedIn(false);
        userRepository.save(user);

        return "Logout successful";
    }
    
    
    public void updateUser(String userIdFromToken, UpdateUserRequest updatedData) {
        User user = userRepository.findByUserId(userIdFromToken)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedData.getName());
        user.setAddress(updatedData.getAddress());
        user.setEmail(updatedData.getEmail());

        userRepository.save(user);
    }
    
    public void deleteByUserId(String userIdFromToken) {
        User user = userRepository.findByUserId(userIdFromToken)
            .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
    
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    
    public User getUserByIdWithAccessControl(Long requestedId, String tokenUserId, Integer userType) throws AccessDeniedException {
        User targetUser = userRepository.findById(requestedId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!targetUser.getUserId().equals(tokenUserId) && userType != 1) {
            throw new AccessDeniedException("Access denied");
        }

        return targetUser;
    }



}
