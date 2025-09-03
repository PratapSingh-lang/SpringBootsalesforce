package com.springBootCoding.CodingTech.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.User;

@Service
public interface RoleService {

	Role addRole(@Valid Role role);

	List<Role> getAllAvailableRoles();

	Role deleteRole(Role role);

	User addListOfRoleToUser(long userId, List<Long> roleId);

	Role updateRole(@Valid Role role);

}
