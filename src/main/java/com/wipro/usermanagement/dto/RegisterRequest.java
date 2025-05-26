package com.wipro.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
	public class RegisterRequest {
	    private String username;
	    private String email;
	    private String password;
	    private String name;
	    private String address;
	    private int userType; // 0 = customer, 1 = admin
	}

