package com.mahmoodidelbi.eventsplatform.api.dtos;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private long userId;
    private String email;
    private String userName;
    private String role;
}
