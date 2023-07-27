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
	private final TokenRepository tokenRepository;
	
	
	/**
	 * Desc : refereshToken this function is refresh the jwt token InputParam :
	 * request,response
	 * 
	 * @return
	 * @throws StreamWriteException
	 * @throws DatabindException
	 * @throws IOException
	 */
	@PostMapping("/refereshtoken")
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
	 * Desc : registerIndivisualuser this function is register the individual user
	 * InpuParam : request
	 * 
	 * @return
	 * @throws RestClientException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@PostMapping("/registerUser")
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
	 * Desc : registerSuperadmin this function is register the super admin InpuParam
	 * : request
	 * 
	 * @return
	 */
	@PostMapping("/registerAdmin")
	public ResponseEntity<?> registeAdmin(@Valid @RequestBody RegisterRequest request) throws MethodArgumentNotValidException
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
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		
			log.info("authenticate method is called");
		
			return ResponseEntity.ok(authenticationService.authenticate(request));
		
	}
	
}
