package com.springBootCoding.CodingTech.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springBootCoding.CodingTech.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.email = ?1")
	Optional<User> findByUsernameWithRoles(String username);

}
