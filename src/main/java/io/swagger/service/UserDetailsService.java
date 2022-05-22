package io.swagger.service;

import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Get user by email
        User user = userRepository.findByEmail(email);

        // Throw exception when no user is found
        if (user == null) {
            throw new UsernameNotFoundException("No user found with " + email + "found.");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(user.getPassword())
                .authorities(Role.ROLE_USER)
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .credentialsExpired(false)
                .build();
    }
}
