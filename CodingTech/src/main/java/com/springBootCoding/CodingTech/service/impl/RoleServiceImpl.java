package com.springBootCoding.CodingTech.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.repo.RoleRepository;
import com.springBootCoding.CodingTech.repo.UserRepository;
import com.springBootCoding.CodingTech.service.RoleService;
import com.springBootCoding.CodingTech.service.UserService;

@Service
public class RoleServiceImpl  implements RoleService{

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public Role addRole(@Valid Role role) {
		role.setName(role.getName().toUpperCase());
		return roleRepository.saveAndFlush(role);
	}

	@Override
	public List<Role> getAllAvailableRoles() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public Role deleteRole(Role roleTobeDeleted) {
		
		Optional<Role> optionalRole = roleRepository.findById(roleTobeDeleted.getId());
	    if (optionalRole.isPresent()) {
	    	
	    	  Role role = optionalRole.get();
	          Set<User> users = role.getUser();

	          // Remove the association with users.
	          for (User user : users) {
	              user.getRole().remove(role);
	          }

	          // Clear the association with users in the role entity (optional, for in-memory updates).
	          role.getUser().clear();

	          // Delete the role from the database.
	          roleRepository.delete(role);
	        
	    } else {
	        throw new EntityNotFoundException("Role not found with id: " + roleTobeDeleted.getId());
	    }
	    return optionalRole.get();
		
	}

	 private User findUserById(long userId) {
		 List<User> userList = userRepo.findAll();
	        return userList.stream()
	                .filter(user -> user.getId() == userId)
	                .findFirst()
	                .orElse(null);
	    }
	 
	@Override
	public User addListOfRoleToUser(long userId, List<Long> roleIdList) {
		// Find the user with the given userId.
        User user = findUserById(userId);

        if (user == null) {
            // User not found. You may want to handle this case accordingly.
            return null;
        }

        // Assuming you have a service or repository that fetches roles based on roleId.
        List<Role> rolesToAdd = getRolesByIdList(roleIdList);

        if (rolesToAdd.isEmpty()) {
            // No roles found for the given roleIdList. You may want to handle this case accordingly.
            return user;
        }

        // Add the roles to the user's existing roles list.
        user.getRole().addAll(rolesToAdd);

        // Save the updated user (if needed) or any other necessary logic here.
        // For this example, I'll just return the updated user.
        return user;
	}

	@Override
	public Role updateRole(@Valid Role role) {
		// Find the existing role in the roleList based on the roleId.
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);

        if (existingRole == null) {
            // Role not found. You may want to handle this case accordingly.
            return null;
        }

        // Update the properties of the existingRole with the properties from the input role.
        // You can choose to update specific properties or update all properties depending on your requirements.
        existingRole.setName(role.getName().toUpperCase());
        existingRole.setDescription(role.getDescription());

        // Save the updated role (if needed) or any other necessary logic here.
        // For this example, I'll just return the updated role.
        return existingRole;
	}
	
	private List<Role> getRolesByIdList(List<Long> roleIdList) {
        // Assuming you have a repository or service that fetches roles based on roleId.
        // For this example, I'll just create some dummy roles.
        List<Role> roles = new ArrayList<>();

        for (Long roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            // Set other properties for the role as needed.
            roles.add(role);
        }

        return roles;
    }

}
