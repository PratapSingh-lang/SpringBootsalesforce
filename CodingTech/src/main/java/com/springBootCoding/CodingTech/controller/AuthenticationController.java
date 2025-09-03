package com.springBootCoding.CodingTech.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.springBootCoding.CodingTech.config.JwtService;
import com.springBootCoding.CodingTech.dto.AuthenticationRequest;
import com.springBootCoding.CodingTech.dto.RegisterRequest;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;
import com.springBootCoding.CodingTech.repo.TokenRepository;
import com.springBootCoding.CodingTech.service.AuthenticationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Desc : AuthenticationController Class exposed the Authentication and
 * Authorization and RefereshToken API's
 * 
 * @author Bhanu Pratap
 *
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final JwtService jwtService;
	
	
	/**
	 * Refreshes the JWT token.
	 *
	 * This method refreshes the JWT token for the user.
	 *
	 * @param request The HttpServletRequest object representing the request
	 * @param response The HttpServletResponse object representing the response
	 * @return ResponseEntity containing the new JWT token and the refresh token with an OK response status
	 * @throws StreamWriteException if there is an issue with writing the response stream
	 * @throws DatabindException if there is an issue with data binding
	 * @throws IOException if an I/O exception occurs
	 */
	@ApiOperation(value = "Refresh JWT token", notes = "Refreshes the JWT token for the user.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "JWT token refreshed successfully"),
	    @ApiResponse(code = 400, message = "Invalid customer data provided"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/refereshToken")
	public ResponseEntity<?> refereshToken(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {
		log.info("refreshing token method called");
		final String authHeader = request.getHeader("Authorization");
		final String refereshToken;
		final String email;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			try {
				refereshToken = authHeader.substring(7);
				email = jwtService.extractUsername(refereshToken);

				String newAccessToken = authenticationService.generateNewAccessToken(email);

				Map<String, String> tokens = new HashMap<>();
				tokens.put("Jwt_token", newAccessToken);
				tokens.put("referesh_token", refereshToken);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				return new ResponseEntity<>(tokens, HttpStatus.OK);
//        	 return ResponseEntity.ok(service.authenticate(request));
			} catch (Exception exception) {
				log.error("No Data Found" + exception.getMessage());
				response.setHeader("error", exception.getMessage());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", exception.getMessage());
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
//        		 new ObjectMapper().writeValue(response.getOutputStream(), error);
				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
			}
		} else {
//        	throw new RuntimeException("Referesh token is missing");
			log.warn("Referesh token is missing");
			return new ResponseEntity<>(new RuntimeException("Referesh token is missing"), HttpStatus.NOT_FOUND);
		}

	}
	
	
	/**
	 * Registers an user with USER role.
	 *
	 * This method registers a new individual user in the system.
	 *
	 * @param request The RegisterRequest object representing the user's registration details
	 * @return ResponseEntity containing the registered user's details with a CREATED response status
	 * @throws IOException if an I/O exception occurs
	 */
	@ApiOperation(value = "Register individual user", notes = "Registers a new user in the system with USER role.")
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "User registered successfully"),
	    @ApiResponse(code = 400, message = "Invalid user data provided"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/registerAsUser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request)
			throws  IOException {
		log.info("register individual user method called");
		
		
		try {
			try {
				return ResponseEntity.ok(authenticationService.registerUser(request));
			} catch (DataNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(ConstraintViolationException  e) {
			String message = e.getMessage();
			e.printStackTrace();
			return new ResponseEntity<>(
					message.substring(message.lastIndexOf("='")+2, message.lastIndexOf("'"))
							, HttpStatus.UNPROCESSABLE_ENTITY);
		}catch (DataIntegrityViolationException e) {
			String errorMessage = e.getCause().getCause().getLocalizedMessage();
			return new ResponseEntity<>(errorMessage.substring(errorMessage.lastIndexOf("Key")+3)
					, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return null;
	}
	
	/**
	 * Registers a user with admin role.
	 *
	 * This method registers a new user with admin role in the system.
	 *
	 * @param request The RegisterRequest object representing the user's registration details
	 * @return ResponseEntity containing the registered user's details with a CREATED response status
	 * @throws MethodArgumentNotValidException if there is an issue with method arguments validation
	 */
	@ApiOperation(value = "Register admin user", notes = "Registers a new user with admin role in the system with ADMIN role.")
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "Admin user registered successfully"),
	    @ApiResponse(code = 400, message = "Invalid user data provided"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/registerAsAdmin")
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request) throws MethodArgumentNotValidException
	{
		log.info("resgister super admin method called");
		
			try {
				return ResponseEntity.ok(authenticationService.registerAdmin(request));
			
			}catch(ConstraintViolationException  e) {
				String message = e.getMessage();
				e.printStackTrace();
				return new ResponseEntity<>(
						message.substring(message.lastIndexOf("='")+2, message.lastIndexOf("'"))
								, HttpStatus.UNPROCESSABLE_ENTITY);
			}catch (DataIntegrityViolationException e) {
				String errorMessage = e.getCause().getCause().getLocalizedMessage();
				return new ResponseEntity<>(errorMessage.substring(errorMessage.lastIndexOf("Key")+3)
						, HttpStatus.UNPROCESSABLE_ENTITY);
			} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
			}
		
	}
	
	
	/**
	 * Authenticates the user with provided credentials.
	 *
	 * This method authenticates the user using the provided authentication request.
	 *
	 * @param request The AuthenticationRequest object representing the user's authentication details
	 * @param httpServletResponse The HttpServletResponse object representing the response
	 * @param httpServletRequest The HttpServletRequest object representing the request
	 * @return ResponseEntity containing the authentication result with an OK response status
	 */
	@ApiOperation(value = "Authenticate user", notes = "Authenticates the user with provided credentials.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "User authenticated successfully"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		
			log.info("authenticate method is called");
		
			return ResponseEntity.ok(authenticationService.authenticate(request));
		
	}
	
}
