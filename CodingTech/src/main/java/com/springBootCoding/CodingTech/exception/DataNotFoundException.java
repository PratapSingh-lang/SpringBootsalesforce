package com.springBootCoding.CodingTech.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
//@Component
public class DataNotFoundException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	public DataNotFoundException(String message) {
		super();
		this.message = message;
	}
	
}
