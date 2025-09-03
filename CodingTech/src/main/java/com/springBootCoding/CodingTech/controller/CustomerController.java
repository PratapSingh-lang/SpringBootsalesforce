package com.springBootCoding.CodingTech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springBootCoding.CodingTech.entity.Customer;
import com.springBootCoding.CodingTech.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Creates a new customer.
     *
     * This method creates a new customer in the system.
     *
     * @param customer The Customer object representing the customer to be created
     * @return ResponseEntity containing the newly created customer and a CREATED response status
     */
    @ApiOperation(value = "Create a new customer", notes = "Creates a new customer in the system. User with ADMIN and USER role can access this Api")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Customer created successfully"),
        @ApiResponse(code = 400, message = "Invalid customer data provided"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @Secured({"ADMIN", "USER"})
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * Get a customer by ID.
     *
     * This method retrieves a customer from the system based on the provided ID.
     *
     * @param id The ID of the customer to be retrieved
     * @return ResponseEntity containing the retrieved customer and an OK response status if found, or NOT_FOUND status if not found
     */
    @ApiOperation(value = "Get a customer by ID", notes = "Retrieves a customer from the system based on the provided ID. User with ADMIN and USER role can access this Api")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Customer retrieved successfully"),
        @ApiResponse(code = 404, message = "Customer not found in the system"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}")
    @Secured({"ADMIN", "USER"})
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all customers.
     *
     * This method retrieves all customers from the system.
     *
     * @return ResponseEntity containing the list of all customers and an OK response status
     */
    @ApiOperation(value = "Get all customers", notes = "Retrieves all customers from the system. User with ADMIN and USER role can access this Api")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Customers retrieved successfully"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    @Secured({"USER","ADMIN"})
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Update a customer by ID.
     *
     * This method updates an existing customer in the system based on the provided ID.
     *
     * @param id The ID of the customer to be updated
     * @param customer The Customer object representing the updated customer data
     * @return ResponseEntity containing the updated customer and an OK response status if updated successfully, or NOT_FOUND status if customer not found
     */
    @ApiOperation(value = "Update a customer by ID", notes = "Updates an existing customer in the system based on the provided ID. User with only ADMIN role can access this Api")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Customer updated successfully"),
        @ApiResponse(code = 400, message = "Invalid customer data provided"),
        @ApiResponse(code = 404, message = "Customer not found in the system"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer != null) {
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a customer by ID.
     *
     * This method deletes a customer from the system based on the provided ID.
     *
     * @param id The ID of the customer to be deleted
     * @return ResponseEntity indicating the result of the deletion operation and an OK response status if deleted successfully, or NOT_FOUND status if customer not found
     */
    @ApiOperation(value = "Delete a customer by ID", notes = "Deletes a customer from the system based on the provided ID. User with only ADMIN role can access this Api")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Customer deleted successfully"),
        @ApiResponse(code = 404, message = "Customer not found in the system"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
}
