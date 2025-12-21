package com.raining.simple_planner.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequestDTO {
    private String accessToken;
    private String refreshToken;
}
