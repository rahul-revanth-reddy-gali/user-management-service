package com.wipro.usermanagement.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String address;
    private String email;
}
