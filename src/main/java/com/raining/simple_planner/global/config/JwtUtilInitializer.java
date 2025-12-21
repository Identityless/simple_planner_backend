package com.raining.simple_planner.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.raining.simple_planner.global.util.TokenUtil;


@Component
public class JwtUtilInitializer {

    public JwtUtilInitializer(
        @Value("${jwt.secret.key}") String secretKey
    ) {
        TokenUtil.init(secretKey);
    }
}

