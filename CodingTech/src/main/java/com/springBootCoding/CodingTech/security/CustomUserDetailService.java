package com.springBootCoding.CodingTech.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBootCoding.CodingTech.entity.CustomUserDetails;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	public CustomUserDetails loadUserByUsername(String username) {
		log.info("loading user by username");
        Optional<User> userOptional = userRepo.findByUsernameWithRoles(username);
        if (userOptional.isEmpty()) {
        	log.warn("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(user.getEmail());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setRoles(user.getRole());
        return customUserDetails;
    }
	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
////	,ResourceNotFoundException 
//	{
//		// TODO Auto-generated method stub
//		User user = this.userRepo.findByEmail(username)
//				.orElseThrow(() ->new UsernameNotFoundException("User not found"));
//		
//		return user;
//	}

}
