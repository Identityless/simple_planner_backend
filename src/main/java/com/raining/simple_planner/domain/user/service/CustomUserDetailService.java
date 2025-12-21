package com.raining.simple_planner.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.raining.simple_planner.domain.user.document.CustomUserDetails;
import com.raining.simple_planner.domain.user.document.User;
import com.raining.simple_planner.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserQueryService userQueryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
        User user = userQueryService.getUserById(username);

        return new CustomUserDetails(user);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User not found with id: " + username);
        }
    }
}
