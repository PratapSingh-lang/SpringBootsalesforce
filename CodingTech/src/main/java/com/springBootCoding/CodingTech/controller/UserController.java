package com.springBootCoding.CodingTech.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springBootCoding.CodingTech.repo.UserRepository;
import com.springBootCoding.CodingTech.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.springBootCoding.CodingTech.entity.Role;
import com.springBootCoding.CodingTech.entity.User;
import com.springBootCoding.CodingTech.exception.DataNotFoundException;
import com.springBootCoding.CodingTech.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Secured("ADMIN")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
	
	/**
	 * Creates a new User by Admin.
	 *
	 * This method adds a new role to the system. Only Admin users can access this API.
	 *
	 * @param user The User object representing the new user to be added
	 * @return ResponseEntity containing the added user and an OK response status
	 */
	@ApiOperation(value = "Create a new role by Admin", notes = "Adds a new role to the system. Only Admin users can access this API.")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Bearer <token>", required = true, dataType = "string", paramType = "header")
    })
	@ApiResponses(value = {
	    @ApiResponse(code = 201, message = "User created successfully"),
	    @ApiResponse(code = 400, message = "Invalid role data provided"),
	    @ApiResponse(code = 403, message = "Access forbidden. Only Admin users can create roles."),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
	    try {
	        // Attempt to add the new role
	        log.info("createUser method is called");
	        User addedUser = userService.createUser(user);

	        // Return a success response with the added role
	        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
	    } catch (IllegalArgumentException e) {
	        // Return a BAD_REQUEST response if the role data is invalid
	        return new ResponseEntity<>("Invalid role data provided", HttpStatus.BAD_REQUEST);
	    }catch(DataIntegrityViolationException e) {
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any other unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	/**
	 * Fetches a list of all users.
	 *
	 * This method retrieves information about all users from the database.
	 *
	 * @return ResponseEntity containing the list of users fetched successfully
	 */
	@ApiOperation(value = "Fetch a list of all users", notes = "Returns a list of all users from the database. Only Admin users can access this API.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Users fetched successfully"),
			@ApiResponse(code = 404, message = "No users found in the database") })
	@GetMapping("/getallusers")
	public ResponseEntity<?> getAllUsers() {
		log.info("getAllUser method is called that willl return UserResponse Object List");
		List<User> returnedUserList = userService.getAllUsersData();
		return ResponseEntity.ok(returnedUserList);
	}

	/**
	 * Fetches the role of a user by their user ID.
	 *
	 * This method retrieves the role assigned to a user based on their user ID.
	 *
	 * @param userId The ID of the user whose role is to be fetched
	 * @return ResponseEntity containing the user's role fetched successfully or a
	 *         NOT_FOUND response if the user is not found
	 */
	@ApiOperation(value = "Fetch the role of a user by their user ID", notes = "Returns the role assigned to a user based on their user ID. Only Admin users can access this API.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User's role fetched successfully"),
			@ApiResponse(code = 404, message = "User not found or user's role not found") })
	@PostMapping("/getuserrole")
	public ResponseEntity<?> getUserRoleByUserId(@RequestParam() long userId) {

		List<Role> returnedRole;
		try {
			returnedRole = userService.getUserRoleByUserId(userId);
			return new ResponseEntity<>(returnedRole, HttpStatus.OK);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * Updates the role(s) of a user by their user ID.
	 *
	 * This method updates the role(s) assigned to a user based on their user ID.
	 *
	 * @param userId  The ID of the user whose role(s) are to be updated
	 * @param rolesId The list of role IDs to be assigned to the user
	 * @return ResponseEntity containing the updated role(s) of the user or a
	 *         NOT_FOUND response if the user is not found
	 */
	@ApiOperation(value = "Update the role(s) of a user by their user ID", notes = "Updates the role(s) assigned to a user based on their user ID. Only Admin users can access this API.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User's role(s) updated successfully"),
			@ApiResponse(code = 400, message = "Invalid user ID or role ID(s) provided"),
			@ApiResponse(code = 404, message = "User not found with the provided user ID") })
	@PutMapping("/updateUserRole")
	public ResponseEntity<?> UpdateUserRole(@RequestParam() long userId, @RequestBody List<Long> rolesId) {

		List<Role> returnedRole;
		try {
			returnedRole = userService.UpdateUserRole(userId, rolesId);
			if (returnedRole == null)
				return new ResponseEntity<>("user not found with user id  :" + userId, HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(returnedRole, HttpStatus.OK);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}
	
	/**
	 * Deletes a User by Admin.
	 *
	 * This method deletes a User from the system. Only Admin users can access this API.
	 *
	 * @param role The User object representing the role to be deleted
	 * @return ResponseEntity indicating the result of the deletion operation and an OK response status
	 */
	@ApiOperation(value = "Delete a user by Admin", notes = "Deletes a user from the system. Only Admin users can access this API.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "user deleted successfully"),
	    @ApiResponse(code = 400, message = "Invalid user data provided"),
	    @ApiResponse(code = 403, message = "Access forbidden. Only Admin users can delete user."),
	    @ApiResponse(code = 404, message = "user not found in the system"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/deleteUser")
	public ResponseEntity<?> deleteRole(@RequestBody User user) {
	    try {
	        // Attempt to delete the role
	        log.info("deleteRole method is called");
	        User deletedUser = userService.deleteUser(user);

	        // Check if the role is not found in the system
	        if (deletedUser == null) {
	            return new ResponseEntity<>("User not found in the system", HttpStatus.NOT_FOUND);
	        }

	        // Return a success response indicating the result of the deletion operation
	        return ResponseEntity.ok(deletedUser);
	    } catch (IllegalArgumentException e) {
	        // Return a BAD_REQUEST response if the role data is invalid
	        return new ResponseEntity<>("Invalid user data provided", HttpStatus.BAD_REQUEST);
	        
	    } catch(EmptyResultDataAccessException e) {
	    	
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
	    }catch (Exception e) {
	        // Return an INTERNAL_SERVER_ERROR response for any other unexpected errors
	        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
