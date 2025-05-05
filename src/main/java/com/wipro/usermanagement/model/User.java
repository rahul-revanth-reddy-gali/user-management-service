package com.wipro.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(nullable=false, unique = true)
	private Long id;
	
	@Column(nullable=false, unique = true)
	private String username;
	
	@Column(nullable=false, unique = true)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	
	private String name;
	
	private String address;
	
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	
	




}
