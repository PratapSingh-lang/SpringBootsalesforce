package com.springBootCoding.CodingTech.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.CustomUserDetails;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CustomUserDetails loadUserByUsername(String username) {
    	log.info("loading user by username");
        Optional<User> userOptional = userRepository.findByUsernameWithRoles(username);
        if (userOptional.isEmpty()) {
        	log.warn("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
//        return CustomUserDetails.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build(); 
        
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(user.getEmail());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setRoles(user.getRole());
        return customUserDetails;
    }
}
