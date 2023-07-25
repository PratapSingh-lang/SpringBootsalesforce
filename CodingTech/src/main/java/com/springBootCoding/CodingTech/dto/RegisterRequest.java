package com.springBootCoding.CodingTech.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	private long id;
	private String title;
//    @Email
	private String email;
	@NotEmpty
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$", message = "Password must contain at least one letter, one digit, and one special character")
	private String password;

}
