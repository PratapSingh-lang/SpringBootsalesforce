package com.springBootCoding.CodingTech.constants;

import org.springframework.stereotype.Component;

@Component
public class VariableConstants {

	public static final String SECRET_KEY = "50655368566D597133743677397A24432646294A404E635166546A576E5A7234";
	public static final String LOGOUT_URL = "/api/v1/auth/logout";
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String USER_ROLE = "USER";
	public static final String ADMIN_ROLE_DESCRIPTION = "This is Admin role";
	public static final String USER_ROLE_DESCRIPTION = "This is User role";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String HEADER = "header";
}
