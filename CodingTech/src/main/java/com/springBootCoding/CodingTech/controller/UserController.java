package com.springBootCoding.CodingTech.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springBootCoding.CodingTech.repo.UserRepository;
import com.springBootCoding.CodingTech.service.AuthenticationService;

import io.swagger.annotations.Api;
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
public class UserController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AuthenticationService service;
	/*
	*//**
		 * Adds a list of privileges to a role by role ID and privilege IDs
		 *
		 * @param roleId       ID of the role to add privileges to
		 * @param privilegeIds IDs of the privileges to add to the role
		 * @return The updated Role object with the added privileges
		 *//*
			 * @ApiOperation(value =
			 * "Fetch information about Privileges assigned to a role based upon role id",
			 * notes = "return a list of privileges assigned to a role ")
			 * 
			 * @ApiResponses(value = {
			 * 
			 * @ApiResponse(code = 200, message =
			 * "Privileges  to role fetched successfully"),
			 * 
			 * @ApiResponse(code = 400, message =
			 * "Invalid role or privilege ID(s) provided"),
			 * 
			 * @ApiResponse(code = 404, message = "Role or privilege(s) not found") })
			 * 
			 * @GetMapping public ResponseEntity<String> sayHello() { return
			 * ResponseEntity.ok("Hello from secured endpoint"); }
			 */

	/**
	 * Fetches a list of all users.
	 *
	 * This method retrieves information about all users from the database.
	 *
	 * @return ResponseEntity containing the list of users fetched successfully
	 */
	@ApiOperation(value = "Fetch a list of all users", notes = "Returns a list of all users from the database")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Users fetched successfully"),
			@ApiResponse(code = 404, message = "No users found in the database") })
	@PostMapping("/getallusers")
	public ResponseEntity<?> getAllUsers() {
		log.info("getAllUser method is called that willl return UserResponse Object List");
		List<User> returnedUserList = service.getAllUsersData();
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
	@ApiOperation(value = "Fetch the role of a user by their user ID", notes = "Returns the role assigned to a user based on their user ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User's role fetched successfully"),
			@ApiResponse(code = 404, message = "User not found or user's role not found") })
	@PostMapping("/getuserrole")
	public ResponseEntity<?> getUserRoleByUserId(@RequestParam() long userId) {

		List<Role> returnedRole;
		try {
			returnedRole = service.getUserRoleByUserId(userId);
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
	@ApiOperation(value = "Update the role(s) of a user by their user ID", notes = "Updates the role(s) assigned to a user based on their user ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User's role(s) updated successfully"),
			@ApiResponse(code = 400, message = "Invalid user ID or role ID(s) provided"),
			@ApiResponse(code = 404, message = "User not found with the provided user ID") })
	@PutMapping("/updateuserrole")
	public ResponseEntity<?> UpdateUserRole(@RequestParam() long userId, @RequestBody List<Long> rolesId) {

		List<Role> returnedRole;
		try {
			returnedRole = service.UpdateUserRole(userId, rolesId);
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

}
