package com.springBootCoding.CodingTech.service;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springBootCoding.CodingTech.entity.Customer;
import com.springBootCoding.CodingTech.repo.CustomerRepository;
import com.springBootCoding.CodingTech.service.impl.CustomerServiceImpl;

public class CustomerServiceImplTest {

	@Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        // Prepare data
        Customer customer = new Customer("1","John Doe", "john.doe@example.com", "1234567890");

        // Mocking behavior 
        when(customerRepository.save(customer)).thenReturn(customer);

        // Perform the operation
        Customer createdCustomer = customerService.createCustomer(customer);

        // Assertions
        assertNotNull(createdCustomer);
        assertNotNull(createdCustomer.getId());
        assertEquals("John Doe", createdCustomer.getName());
        assertEquals("john.doe@example.com", createdCustomer.getEmail());
        assertEquals("1234567890", createdCustomer.getPhone());

        // Verify that the save method was called once
        verify(customerRepository).save(customer);
    }
  
    @Test
    void testGetCustomerById() {
        // Prepare data
        String customerId = "123";
        Customer customer = new Customer("2","John Doe", "john.doe@example.com", "1234567890");
        customer.setId(customerId);

        // Mocking behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Perform the operation
        Customer retrievedCustomer = customerService.getCustomerById(customerId);

        // Assertions
        assertNotNull(retrievedCustomer);
        assertEquals(customerId, retrievedCustomer.getId());
        assertEquals("John Doe", retrievedCustomer.getName());
        assertEquals("john.doe@example.com", retrievedCustomer.getEmail());
        assertEquals("1234567890", retrievedCustomer.getPhone());

        // Verify that the findById method was called once
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testGetCustomerById_NotFound() {
        // Prepare data
        String customerId = "123";

        // Mocking behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Perform the operation
        Customer retrievedCustomer = customerService.getCustomerById(customerId);

        // Assertions
        assertNull(retrievedCustomer);

        // Verify that the findById method was called once
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testGetAllCustomers() {
        // Prepare data
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("2", "John Doe", "john.doe@example.com", "1234567890"));
        customers.add(new Customer("3", "Jane Smith", "jane.smith@example.com", "9876543210"));

        // Mocking behavior
        when(customerRepository.findAll()).thenReturn(customers);

        // Perform the operation
        List<Customer> retrievedCustomers = customerService.getAllCustomers();

        // Assertions
        assertNotNull(retrievedCustomers);
        assertEquals(customers.size(), retrievedCustomers.size());

        // Verify that the findAll method was called once
        verify(customerRepository).findAll();
    }

    @Test
    void testUpdateCustomer() {
        // Prepare data
        String customerId = "123";
        Customer existingCustomer = new Customer("123", "John Doe", "john.doe@example.com", "1234567890");
        existingCustomer.setId(customerId);
        Customer updatedCustomer = new Customer("123" ,"John Doe Updated", "john.doe.updated@example.com", "9876543210");

        // Mocking behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(updatedCustomer);
 
        // Perform the operation
        Customer resultCustomer = customerService.updateCustomer(customerId, updatedCustomer);
 
        // Assertions
        assertNotNull(resultCustomer);
        assertEquals(customerId, resultCustomer.getId());
        assertEquals("John Doe Updated", resultCustomer.getName());
        assertEquals("john.doe.updated@example.com", resultCustomer.getEmail());
        assertEquals("9876543210", resultCustomer.getPhone());

        // Verify that the findById and save methods were called once each
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(existingCustomer);
    } 
 
    @Test
    void testUpdateCustomer_NotFound() {
        // Prepare data
        String customerId = "123";
        Customer updatedCustomer = new Customer("123", "John Doe Updated", "john.doe.updated@example.com", "9876543210");

        // Mocking behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Perform the operation
        Customer resultCustomer = customerService.updateCustomer(customerId, updatedCustomer);

        // Assertions
        assertNull(resultCustomer);

        // Verify that the findById method was called once
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testDeleteCustomer() {
        // Prepare data
        String customerId = "123";
        Customer customer = new Customer("123", "John Doe", "john.doe@example.com", "1234567890");
        customer.setId(customerId);

        // Mocking behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Perform the operation
        boolean isDeleted = customerService.deleteCustomer(customerId);

        // Assertions
        assertTrue(isDeleted);

        // Verify that the findById and delete methods were called once each
        verify(customerRepository).findById(customerId);
        verify(customerRepository).delete(customer);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        // Prepare data
        String customerId = "123";

        // Mocking behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Perform the operation
        boolean isDeleted = customerService.deleteCustomer(customerId);

        // Assertions
        assertFalse(isDeleted);

        // Verify that the findById method was called once
        verify(customerRepository).findById(customerId);
    }
	
}
