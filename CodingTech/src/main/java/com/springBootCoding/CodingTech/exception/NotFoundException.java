package com.springBootCoding.CodingTech.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@Component
public class NotFoundException extends Exception {

	String message;
}
