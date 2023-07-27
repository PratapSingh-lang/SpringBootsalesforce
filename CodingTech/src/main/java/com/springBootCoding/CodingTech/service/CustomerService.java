package com.springBootCoding.CodingTech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springBootCoding.CodingTech.entity.Customer;

@Service
public interface CustomerService {

	Customer createCustomer(Customer customer);

	Customer getCustomerById(String id);

	List<Customer> getAllCustomers();

	Customer updateCustomer(String id, Customer customer);

	boolean deleteCustomer(String id);

}
