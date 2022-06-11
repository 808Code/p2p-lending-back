package com.bonsai.accountservice.config;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialRepo userCredentialRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        UserCredential user = userCredentialRepo.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        UserDetails userDetails = new User(user.getEmail(), user.getPassword(),
                true, true, true,
                true, null);

        return userDetails;
    }
}