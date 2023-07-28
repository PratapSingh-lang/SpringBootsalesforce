package com.springBootCoding.CodingTech.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.constants.VariableConstants;
import com.springBootCoding.CodingTech.entity.CustomUserDetails;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.repo.UserRepository;

@Slf4j
@Service
public class JwtService {

	@Autowired
	UserRepository userRepository;

	public String generateRefershToken(User userDetails) {
		log.info("Generating refresh token");
		CustomUserDetails customUserDetails = new CustomUserDetails();
		customUserDetails.setUsername(userDetails.getEmail());
		return Jwts.builder().setSubject(customUserDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000000 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		log.info("extracting user name");
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		log.info("extracting claim");
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(User userDetails) {
		log.info("Generating token");
		Map<String, Collection<? extends GrantedAuthority>> claims = new HashMap<>();

		CustomUserDetails customUserDetails = new CustomUserDetails();
		customUserDetails.setUsername(userDetails.getEmail());
		customUserDetails.setPassword(userDetails.getPassword());
		customUserDetails.setRoles(userDetails.getRole());
		claims.put(customUserDetails.getUsername(), customUserDetails.getAuthorities());

		return generateToken(claims, customUserDetails);
	}

	public String generateToken(Map<String, Collection<? extends GrantedAuthority>> extraClaims,
			UserDetails userDetails) {
		log.info("Generating token");
		return Jwts.builder().setClaims(extraClaims)
//        .setClaims(userDetails.getAuthorities())
				.setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		log.info("validating the token");
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		log.info("verifying the token is expired or not");
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		log.info("extracting the expiration");
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		log.info("extracting all cliams");
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		log.info("getting sign in key");
		byte[] keyBytes = Decoders.BASE64.decode(VariableConstants.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean checkPrivilege(String email, String privilegeName) {
		log.info("checking privileges");
		User user = userRepository.findByEmail(email).get();
		privilegeName = privilegeName.substring(privilegeName.lastIndexOf("/") + 1);
		log.info("privilegeName that came API Is : {} ", privilegeName);
		if (user == null) {
			return false;
		}
		
		
		return false;
	}
}
