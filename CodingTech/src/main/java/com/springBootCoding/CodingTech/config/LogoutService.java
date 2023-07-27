package com.springBootCoding.CodingTech.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.repo.TokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler  {

	  private final TokenRepository tokenRepository;
	 @Override
	  public void logout(
	      HttpServletRequest request,
	      HttpServletResponse response,
	      Authentication authentication
	  ) {
		 log.info("logging out");
	    final String authHeader = request.getHeader("Authorization");
	    final String jwt;
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    jwt = authHeader.substring(7);
	    var storedToken = tokenRepository.findByToken(jwt)
	        .orElse(null);
	    if (storedToken != null) {
	    	log.info("Tocken Expired");
	      storedToken.setExpired(true);
	      log.info("Tocken Revoked");
	      storedToken.setRevoked(true);
	      tokenRepository.save(storedToken);
	      SecurityContextHolder.clearContext();
	    }
	  }
}
