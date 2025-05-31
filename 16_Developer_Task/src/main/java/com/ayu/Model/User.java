package com.ayu.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="Users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotBlank(message = "invalid username")
	@Column(unique = true)
	private String username;
	@NotBlank(message = "invalid password")
	private String password;
	
	private String role;
	
	private boolean isActive;
	
	private LocalDateTime createdAt;
		
}
