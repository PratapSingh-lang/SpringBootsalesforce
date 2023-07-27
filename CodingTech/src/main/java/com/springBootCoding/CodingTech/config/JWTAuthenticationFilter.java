package com.springBootCoding.CodingTech.config;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.Token;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.repo.TokenRepository;
import com.springBootCoding.CodingTech.repo.UserRepository;

//import com.bel.nms.auth.jwt.JWTTokenValidatorFilter;

//import com.bel.nms.auth.jwt.authConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

//		String jwt = parseJwt(request);
//		logger.info("Token === "+jwt);
	
		final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

		log.info("Inside do filter intenal method");
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String email;
		String privilegeName;
		boolean isTokenValid = false;
		request.getHeader("Bearer");
		if (authHeader == null || !authHeader.startsWith("Bearer ")
				|| request.getServletPath().equals("/api/v1/auth/refereshtoken")) {
			log.info("value of authHeader is : {}" ,  authHeader);
			filterChain.doFilter(request, response);
			return;
		}
		log.info("Request  Uri is : {}", request.getRequestURI());
		log.info("Request ServletPath equals : {}",  request.getServletPath());
		log.info("value of authHeader is : {}", authHeader);
		String calledAPiPath = request.getHeader("CalledAPi");
		if (calledAPiPath == null)
			privilegeName = request.getServletPath();
		else {
			privilegeName = calledAPiPath;
		}
		jwtToken = authHeader.substring(7);
		if (jwtToken != null) {
			try {
				email = jwtService.extractUsername(jwtToken);
				log.info("Username from token is  : {}", email);
				if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(email);
					User user2 = userRepository.findByEmail(email).get();
					Token token = tokenRepository.findByToken(jwtToken).get();
					isTokenValid = tokenRepository.findByToken(jwtToken).map(t -> !t.isExpired() && !t.isRevoked())
							.orElse(false);
					log.info("token revoked status : {}", isTokenValid);

					User user = userRepository.findByUsernameWithRoles(email).get();
					Set<Role> roles = user.getRole();
					boolean roleName = false;
					for(Role role : roles) {
						if(role.getName().equals("SUPERADMIN")) {
							roleName = true;
						}
					}
					
//					if (isTokenValid) {
//						if (!roleName) {
//							response.setHeader("error", "user don't have access to this api");
//							Map<String, String> error = new HashMap<>();
//							error.put("error_message", "user don't have access to this api");
//							response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
//							new ObjectMapper().writeValue(response.getOutputStream(), error);
//							throw new RuntimeException("user don't have access to this api");
//						}
//					}

					if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					} else if (!isTokenValid) {
						response.setHeader("error", "user loggoed out , needs to login again");
						Map<String, String> error = new HashMap<>();
						error.put("error_message", "user loggoed out , needs to login again");
						response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
						new ObjectMapper().writeValue(response.getOutputStream(), error);
						throw new RuntimeException("user needs to login again");
					}
				}
			} catch (ExpiredJwtException e) {
				log.error(e.getMessage());
				// e.printStackTrace();
				Claims claims = e.getClaims();
				System.out.println("Token Expired " + String.valueOf(claims.get(
//							authConstants.JWT_CLAIM_USERNAME
						"userName")));
				response.setHeader("error", e.getMessage());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", e.getMessage());
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);

//       		 response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
////       		 return new ResponseEntity<>(
//       				 throw new RuntimeException("token expired");
//           			 , HttpStatus.NOT_FOUND);
//				response.set
//				try {
//
//					logger.info("inside try block");
////						response = verifyRefreshToken(request, response, claims);
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					// e1.printStackTrace();
//					
//				}
			} catch (Exception e) {

				logger.error("JWT claims string is empty: {}", e.getMessage());
				// throw new AuthenticationServiceException("Authentication failed");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

//	private HttpServletResponse verifyRefreshToken(HttpServletRequest request, HttpServletResponse response,Claims claims) {
//		String username = String.valueOf(claims.get(authConstants.JWT_CLAIM_USERNAME));
//		String authToken = parseJwt(request);
//		String newRefreshToken = refreshTokenUtil.validateAndGenerateRefreshToken(authToken);
//		if(newRefreshToken!=null)
//		{
//		Authentication auth = new UsernamePasswordAuthenticationToken(username, null);
//		SecurityContextHolder.getContext().setAuthentication(auth);
//		String newjwt = jwtUtils.generateJwtToken(username);
//		response.setHeader(authConstants.AUTHORIZATION, newjwt);
//		response.setHeader(authConstants.REFRESHTOKEN, newRefreshToken);
//		response.setHeader(authConstants.TOKEN_CHANGED, "true");
//		return response;
//		}else
//		{
//			throw new AuthenticationServiceException("Refresh Token Expired");
//		}
//		
//	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(
//				authConstants.AUTHORIZATION
				"Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		} else
			return null;
	}

	private void validateJWTToken(String jwt) {
		// TODO Auto-generated method stub

	}
}
