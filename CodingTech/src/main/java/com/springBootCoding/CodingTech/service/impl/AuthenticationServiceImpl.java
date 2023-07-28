package com.springBootCoding.CodingTech.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.config.JwtService;
import com.springBootCoding.CodingTech.constants.VariableConstants;
import com.springBootCoding.CodingTech.dto.AuthenticationRequest;
import com.springBootCoding.CodingTech.dto.AuthenticationResponse;
import com.springBootCoding.CodingTech.dto.RegisterRequest;
import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.Token;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.enums.TokenType;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;
import com.springBootCoding.CodingTech.repo.RoleRepository;
import com.springBootCoding.CodingTech.repo.TokenRepository;
import com.springBootCoding.CodingTech.repo.UserRepository;
import com.springBootCoding.CodingTech.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;
	private final RoleRepository roleRepository;
	private final JwtService jwtService;
	private final TokenRepository tokenRepository;
	private final AuthenticationManager authManager;

	@Override
	public AuthenticationResponse registerAdmin(@Valid RegisterRequest request) throws DataNotFoundException {

		log.info(" Email during registration is : " + request.getEmail());
		User user = buildUserObject(request);
		User savedUser = userRepo.save(user);
		Role findByRoleName = roleRepository.findByName(VariableConstants.ADMIN_ROLE);
		User addedRoleToUser;
		if (findByRoleName == null) {
			Role role = new Role();
			role.setName(VariableConstants.ADMIN_ROLE);
			role.setDescription(VariableConstants.ADMIN_ROLE_DESCRIPTION);

			Role savedRole = roleRepository.save(role);
			List<Long> roleId = new ArrayList<>();
			roleId.add(savedRole.getId());
			addedRoleToUser = addListOfRoleToUser(savedUser.getId(), roleId);
		} else {
			List<Long> roleId = new ArrayList<>();
			roleId.add(findByRoleName.getId());
			addedRoleToUser = addListOfRoleToUser(savedUser.getId(), roleId);
		}
		String jwtToken = jwtService.generateToken(addedRoleToUser);
		String referesh_token = jwtService.generateRefershToken(addedRoleToUser);
		saveUserToken(addedRoleToUser, jwtToken);

		return AuthenticationResponse.builder().token(jwtToken).user(addedRoleToUser).referesh_token(referesh_token)
				.build();

	}

	private void saveUserToken(User user, String jwtToken) {

		log.info("saving user token");
		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
				.build();
		System.out.println("value of token is ::: " + token.toString());
		Token savedToken = tokenRepository.save(token);
		log.info("Saved token id is : {}", savedToken.getId());
		log.info("jwt token is : {}", savedToken.getToken());
	}

	private User addListOfRoleToUser(Long userId, List<Long> roleId) throws DataNotFoundException {

		log.info("adding list of roles to user");
		User user = userRepo.findById(userId).orElse(null);

		if (user == null) {
			log.warn("Student not found");
			throw new DataNotFoundException("Student not found");
		}

		List<Role> findAllRolesById = roleRepository.findAllById(roleId);
		// Process the Role IDs and add them to the student
		Set<Role> Roles = new HashSet<>();
		Roles.addAll(findAllRolesById);
		if (Roles.isEmpty()) {
			log.warn("No Roles found with the given IDs");
			throw new DataNotFoundException("No Roles found with the given IDs");
		}

		Set<Role> existingRoles = user.getRole();
		if (existingRoles != null) {
			existingRoles.addAll(Roles);
			user.setRole(existingRoles);
		} else {
			user.setRole(Roles);
		}
		User savedUser = userRepo.save(user);
		return savedUser;

	}

	private User buildUserObject(@Valid RegisterRequest request) {

		User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.build();
		return user;

	}

	@Override
	public String generateNewAccessToken(String email) {

		log.info("generating new access token");
		User user = userRepo.findByEmail(email).orElseThrow();
		String jwtToken = jwtService.generateToken(user);

		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);

		return jwtToken;

	}

	private void revokeAllUserTokens(User user) {

		log.info("revoke all user tokens");
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);

	}

	@Override
	public AuthenticationResponse registerUser(@Valid RegisterRequest request) throws DataNotFoundException {

		log.info(" Email during registration is : " + request.getEmail());
		User user = buildUserObject(request);
		User savedUser = userRepo.save(user);
		Role findByRoleName = roleRepository.findByName(VariableConstants.USER_ROLE);
		User addedRoleToUser;
		if (findByRoleName == null) {
			Role role = new Role();
			role.setName(VariableConstants.USER_ROLE);
			role.setDescription(VariableConstants.USER_ROLE_DESCRIPTION);

			Role savedRole = roleRepository.save(role);
			List<Long> roleId = new ArrayList<>();
			roleId.add(savedRole.getId());
			addedRoleToUser = addListOfRoleToUser(savedUser.getId(), roleId);
		} else {
			List<Long> roleId = new ArrayList<>();
			roleId.add(findByRoleName.getId());
			addedRoleToUser = addListOfRoleToUser(savedUser.getId(), roleId);
		}
		String jwtToken = jwtService.generateToken(addedRoleToUser);
		String referesh_token = jwtService.generateRefershToken(addedRoleToUser);
		saveUserToken(addedRoleToUser, jwtToken);

		return AuthenticationResponse.builder().token(jwtToken).user(addedRoleToUser).referesh_token(referesh_token)
				.build();

	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		log.info("Email during sign in is : " + request.getEmail());

		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = userRepo.findByEmail(request.getEmail()).orElseThrow();
		String jwtToken = jwtService.generateToken(user);
		String referesh_token = jwtService.generateRefershToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder().token(jwtToken).user(user).referesh_token(referesh_token).build();

	}

}
