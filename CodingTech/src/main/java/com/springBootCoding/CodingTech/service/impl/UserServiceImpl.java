package com.springBootCoding.CodingTech.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.Token;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;
import com.springBootCoding.CodingTech.exception.NotFoundException;
import com.springBootCoding.CodingTech.repo.RoleRepository;
import com.springBootCoding.CodingTech.repo.TokenRepository;
import com.springBootCoding.CodingTech.repo.UserRepository;
import com.springBootCoding.CodingTech.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Component
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private  TokenRepository tokenRepository;
	@Override
	public List<User> getAllUsersData() {
		List<User> usersList = userRepo.findAll();
		
		return usersList;
	}

	@Override
	public List<Role> getUserRoleByUserId(long userId) throws DataNotFoundException {
		User user = userRepo.findById(userId).orElse(null);
		if (user == null) {
			throw new DataNotFoundException("User not found with userId : " + userId);
		}
		
		Set<Role> roleSet = user.getRole();
		
		List<Role> roleList = new ArrayList<>();
		if (roleSet != null) {
			roleSet.forEach(roles -> {
				Role role = new Role();
				role.setId(roles.getId());
				role.setDescription(roles.getDescription());
				role.setName(roles.getName());
				roleList.add(role);
			});
		}
		return roleList;
	}

	@Override
	public List<Role> UpdateUserRole(long userId, List<Long> rolesId) throws DataNotFoundException, NotFoundException {
		log.info("adding list of roles to user");
		User user = userRepo.findById(userId).orElse(null);

		if (user == null) {
			log.info("User not found");
			throw new DataNotFoundException("User not found with userId : " + userId);
		}

		List<Role> findAllRolesById = roleRepository.findAllById(rolesId);
		// Process the Role IDs and add them to the User
		Set<Role> Roles = new HashSet<>();
		Roles.addAll(findAllRolesById);
		if (Roles.isEmpty()) {
			log.warn("No Roles found with the given IDs");
			throw new NotFoundException("No Roles found with the given IDs");
		}
		user.setRole(Roles);
		userRepo.save(user);
		return getUserRoleByUserId(user.getId());
	}

	@Override
	public User createUser(@Valid User user) {
		
		@Valid
		User savedUser = userRepo.saveAndFlush(user);
		return savedUser;
	}

	@Override
	public User deleteUser(User user) {
		
		Optional<User> findById = userRepo.findById(user.getId());
		if(findById != null) {
			
			User existedUser = findById.get();
			existedUser.getRole().clear();
			List<Token> tokens = existedUser.getTokens();
			if (tokens.size() > 0)
				tokenRepository.deleteAllInBatch(tokens);
			existedUser.getTokens().clear();
			userRepo.delete(existedUser);
		}
		return findById.get();
	}

}
