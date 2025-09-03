package com.springBootCoding.CodingTech.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.dto.AuthenticationRequest;
import com.springBootCoding.CodingTech.dto.AuthenticationResponse;
import com.springBootCoding.CodingTech.dto.RegisterRequest;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;

@Service
public interface AuthenticationService {

	AuthenticationResponse registerUser(@Valid RegisterRequest request) throws DataNotFoundException;

	String generateNewAccessToken(String email);

	AuthenticationResponse registerAdmin(@Valid RegisterRequest request) throws DataNotFoundException;

	AuthenticationResponse authenticate(AuthenticationRequest request);

}
