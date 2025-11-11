package com.mahmoodidelbi.eventsplatform.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {
    private String accessToken;
}
