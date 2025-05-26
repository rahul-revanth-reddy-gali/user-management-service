package com.wipro.usermanagement.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.usermanagement.dto.AuthenticationRequest;
import com.wipro.usermanagement.dto.AuthenticationResponse;
import com.wipro.usermanagement.dto.RegisterRequest;
import com.wipro.usermanagement.dto.UpdateUserRequest;
import com.wipro.usermanagement.service.AuthenticationService;
import com.wipro.usermanagement.util.JwtService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final JwtService jwtService;
    
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        String result = authService.logout(token);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody UpdateUserRequest updatedData) {

        String token = authHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(token);
        String tokenUserId = claims.get("userId", String.class);

        authService.updateUser(tokenUserId, updatedData);

        return ResponseEntity.ok("User updated successfully");
    }
    
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteOwnAccount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtService.extractAllClaims(token).get("userId", String.class);

        authService.deleteByUserId(userId);

        return ResponseEntity.ok("User deleted successfully");
    }
    
    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Integer userType = jwtService.extractAllClaims(token).get("userType", Integer.class);

        if (userType == null || userType != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins only");
        }

        return ResponseEntity.ok(authService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestHeader("Authorization") String authHeader) throws AccessDeniedException {
        String token = authHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(token);
        String tokenUserId = claims.get("userId", String.class);
        Integer userType = claims.get("userType", Integer.class);

        return ResponseEntity.ok(authService.getUserByIdWithAccessControl(id, tokenUserId, userType));
    }

    

}
