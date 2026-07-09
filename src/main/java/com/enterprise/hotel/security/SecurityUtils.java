package com.enterprise.hotel.security;

import com.enterprise.hotel.common.exception.BusinessException;
import com.enterprise.hotel.user.User;
import com.enterprise.hotel.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/** Convenience accessor for the currently authenticated user. */
@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails ud) {
            return userRepository.findByEmail(ud.getUsername())
                    .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Session user not found"));
        }
        throw new BusinessException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    }
}
