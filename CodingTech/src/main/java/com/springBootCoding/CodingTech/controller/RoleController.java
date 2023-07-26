package com.springBootCoding.CodingTech.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;
import com.springBootCoding.CodingTech.service.RoleService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleController {

	private final RoleService roleService;

	/**
	 * Creates a new role by Admin.
	 *
	 * This method adds a new role to the system. Only Admin users can access this API.
	 *
	 * @param role The Role object representing the new role to be added
	 * @return ResponseEntity containing the added role and an OK response status
	 */
	@ApiOperation(value = "Create a new role by Admin", notes = "Adds a new role to the system. Only Admin users can access this API.")
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "Role created successfully"),
	    @ApiResponse(code = 400, message = "Invalid role data provided"),
	    @ApiResponse(code = 403, message = "Access forbidden. Only Admin users can create roles."),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/createrole")
	// @Secured("createrole") // Uncomment this line to enable role-based access control
	public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {
	    try {
	        // Attempt to add the new role
	        log.info("createRole method is called");
	        Role addedRole = roleService.addRole(role);

	        // Return a success response with the added role
	        return new ResponseEntity<>(addedRole, HttpStatus.CREATED);
	    } catch (IllegalArgumentException e) {
	        // Return a BAD_REQUEST response if the role data is invalid
	        return new ResponseEntity<>("Invalid role data provided", HttpStatus.BAD_REQUEST);
	    } catch(ConstraintViolationException e) {
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
	    } catch(DataIntegrityViolationException e) {
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any other unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	/**
	 * Updates an existing role by Admin.
	 *
	 * This method updates an existing role in the system. Only Admin users can access this API.
	 *
	 * @param role The Role object representing the role to be updated
	 * @return ResponseEntity containing the updated role and an OK response status
	 */
	@ApiOperation(value = "Update an existing role by Admin", notes = "Updates an existing role in the system. Only Admin users can access this API.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Role updated successfully"),
	    @ApiResponse(code = 400, message = "Invalid role data provided"),
	    @ApiResponse(code = 403, message = "Access forbidden. Only Admin users can update roles."),
	    @ApiResponse(code = 404, message = "Role not found in the system"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping("/updaterole")
	// @Secured("updaterole") // Uncomment this line to enable role-based access control
	public ResponseEntity<?> updateRole(@Valid @RequestBody Role role) {
	    try {
	        // Attempt to update the existing role
	        log.info("updateRole method is called");
	        Role updatedRole = roleService.updateRole(role);

	        // Check if the role is not found in the system
	        if (updatedRole == null) {
	            return new ResponseEntity<>("Role not found in the system", HttpStatus.NOT_FOUND);
	        }

	        // Return a success response with the updated role
	        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
	    } catch (IllegalArgumentException e) {
	        // Return a BAD_REQUEST response if the role data is invalid
	        return new ResponseEntity<>("Invalid role data provided", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any other unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	/**
	 * Adds a list of roles to a user.
	 *
	 * This method assigns a list of roles to a user based on their user ID and the provided role IDs.
	 *
	 * @param roleId The list of role IDs to be assigned to the user
	 * @param userId The ID of the user to whom roles are to be added
	 * @return ResponseEntity containing the updated user with the added roles and an OK response status
	 */
	@ApiOperation(value = "Add a list of roles to a user", notes = "Assigns a list of roles to a user based on their user ID and the provided role IDs.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Roles added to the user successfully"),
	    @ApiResponse(code = 400, message = "Invalid user ID or role ID(s) provided"),
	    @ApiResponse(code = 404, message = "User not found with the provided user ID"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/addlistofroletouser")
	// @Secured("addlistofroletouser") // Uncomment this line to enable role-based access control
	public ResponseEntity<?> addListOfRoleToUser(@RequestBody List<Long> roleId, @RequestParam("id") long userId) {
	    try {
	        // Attempt to add the list of roles to the user
	        log.info("addListOfRoleToUser method is called");
	        User addUserToRole = roleService.addListOfRoleToUser(userId, roleId);

	        // Check if the user is not found with the provided user ID
	        if (addUserToRole == null) {
	            return new ResponseEntity<>("User not found with user ID: " + userId, HttpStatus.NOT_FOUND);
	        }

	        // Return a success response with the updated user containing the added roles
	        return new ResponseEntity<>(addUserToRole, HttpStatus.OK);
	    } catch (IllegalArgumentException e) {
	        // Return a BAD_REQUEST response if the user ID or role ID(s) are invalid
	        return new ResponseEntity<>("Invalid user ID or role ID(s) provided", HttpStatus.BAD_REQUEST);
	    
	    } catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any other unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	/**
	 * Retrieves all available roles.
	 *
	 * This method fetches a list of all available roles in the system.
	 *
	 * @return ResponseEntity containing the list of all available roles and an OK response status
	 */
	@ApiOperation(value = "Retrieve all available roles", notes = "Fetches a list of all available roles in the system.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "All available roles fetched successfully"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/getallavailableroles")
	// @Secured("getallavailableroles") // Uncomment this line to enable role-based access control
	public ResponseEntity<?> getAllAvailableRoles() {
	    try {
	        // Fetch a list of all available roles
	        log.info("getAllAvailableRoles method is called");
	        List<Role> roleList = roleService.getAllAvailableRoles();

	        // Return a success response with the list of all available roles
	        return new ResponseEntity<>(roleList, HttpStatus.OK);
	    } catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	/**
	 * Deletes a role by Admin.
	 *
	 * This method deletes a role from the system. Only Admin users can access this API.
	 *
	 * @param role The Role object representing the role to be deleted
	 * @return ResponseEntity indicating the result of the deletion operation and an OK response status
	 */
	@ApiOperation(value = "Delete a role by Admin", notes = "Deletes a role from the system. Only Admin users can access this API.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Role deleted successfully"),
	    @ApiResponse(code = 400, message = "Invalid role data provided"),
	    @ApiResponse(code = 403, message = "Access forbidden. Only Admin users can delete roles."),
	    @ApiResponse(code = 404, message = "Role not found in the system"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/deleterole")
	// @Secured("deleterole") // Uncomment this line to enable role-based access control
	public ResponseEntity<?> deleteRole(@RequestBody Role role) {
	    try {
	        // Attempt to delete the role
	        log.info("deleteRole method is called");
	        Role deletedRole = roleService.deleteRole(role);

	        // Check if the role is not found in the system
	        if (deletedRole == null) {
	            return new ResponseEntity<>("Role not found in the system", HttpStatus.NOT_FOUND);
	        }

	        // Return a success response indicating the result of the deletion operation
	        return ResponseEntity.ok(deletedRole);
	    } catch (IllegalArgumentException e) {
	        // Return a BAD_REQUEST response if the role data is invalid
	        return new ResponseEntity<>("Invalid role data provided", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any other unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}
