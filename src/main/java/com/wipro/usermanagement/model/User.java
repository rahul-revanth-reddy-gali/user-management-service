package com.wipro.usermanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // internal DB id

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(nullable = false, unique = true)
    private String userId; // ✅ Business ID for external reference (UUID or custom)

    private String address;

    private int userType; // ✅ 1 = admin, 0 = customer
    
    private String role;

    private boolean isLoggedIn; // ✅ for login status tracking
}
