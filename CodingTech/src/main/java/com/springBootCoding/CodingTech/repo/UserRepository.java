package com.springBootCoding.CodingTech.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springBootCoding.CodingTech.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{

}
