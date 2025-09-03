package com.springBootCoding.CodingTech.entity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@Column(unique = true, nullable = false)
	private String id;
    private String name;
    private String email;
    private String phone;
	
    public void generateAndSetId() {
        this.id = UUID.randomUUID().toString();
    }
    
}
