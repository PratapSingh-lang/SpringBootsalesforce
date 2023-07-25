package com.springBootCoding.CodingTech.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springBootCoding.CodingTech.enums.TokenType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

	  @Id
	  @GeneratedValue
	  private long id;

	  @Column(unique = true)
	  private String token;

	  @Enumerated(EnumType.STRING)
	  private TokenType tokenType = TokenType.BEARER;

	  private boolean revoked;

	  private boolean expired;

	  @ManyToOne
	  @JsonIgnore
	  @JoinColumn(name = "user_id")
	  public User user;
}
