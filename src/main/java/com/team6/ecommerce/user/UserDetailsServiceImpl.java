package com.team6.ecommerce.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        //Try finding user by email first
        User user = userRepository.findByEmail(identifier);

        //If user is not found by email, try finding by phone number
        if (user == null) {
            user = userRepository.findByPhone(identifier);
        }

        //If user is still null, throw UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email or phone: " + identifier);
        }

        //Return the user, which should implement UserDetails if it somehow got to this stage.
        return user;
    }

}
