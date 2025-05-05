package com.wipro.usermanagement.dto;

import com.wipro.usermanagement.model.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterRequest {
	
	private String username;
	private String email;
	private String password;
	private String name;
	private String address;
	private Role role;
	

}
