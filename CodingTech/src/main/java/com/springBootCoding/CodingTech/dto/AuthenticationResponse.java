package com.springBootCoding.CodingTech.dto;



import com.springBootCoding.CodingTech.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

	   private String token;
	    
	    private String referesh_token;
	    private User user;
	
}
