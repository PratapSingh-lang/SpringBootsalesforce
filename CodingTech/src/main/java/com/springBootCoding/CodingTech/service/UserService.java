package com.springBootCoding.CodingTech.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;
import com.springBootCoding.CodingTech.exception.NotFoundException;


@Service
@Component
public interface UserService {

	List<User> getAllUsersData();

	List<Role> getUserRoleByUserId(long userId) throws DataNotFoundException;

	List<Role> UpdateUserRole(long userId, List<Long> rolesId) throws DataNotFoundException, NotFoundException;

	User createUser(@Valid User user);

	User deleteUser(User user);

	
	

}
