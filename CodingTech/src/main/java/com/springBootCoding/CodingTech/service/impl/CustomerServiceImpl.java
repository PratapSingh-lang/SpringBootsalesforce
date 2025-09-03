/**
 * 
 */
package com.springBootCoding.CodingTech.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.Customer;
import com.springBootCoding.CodingTech.repo.CustomerRepository;
import com.springBootCoding.CodingTech.service.CustomerService;

import lombok.AllArgsConstructor;

/**
 * 
 */
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	
	@Override
    public Customer createCustomer(Customer customer) {
		customer.generateAndSetId();
        // Save the new customer to the database
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(String id) {
        // Retrieve a customer from the database by ID
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> getAllCustomers() {
        // Retrieve all customers from the database
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(String id, Customer customer) {
        // Check if the customer with the given ID exists
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            // Update the existing customer with the new data
            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            // Save the updated customer to the database
            return customerRepository.save(existingCustomer);
        } else {
            // Customer with the given ID not found, return null or throw an exception
            return null;
        }
    }

    @Override
    public boolean deleteCustomer(String id) {
        // Check if the customer with the given ID exists
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            // Delete the customer from the database
            customerRepository.delete(customer);
            return true;
        } else {
            // Customer with the given ID not found, return false or throw an exception
            return false;
        }
    }

}
