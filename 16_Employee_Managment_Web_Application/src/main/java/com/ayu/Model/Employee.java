package com.ayu.Model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="middle_name")
	private String middleName;
	
	@Column(name="empID",unique = true)
	private String empId;
	
	@DateTimeFormat(pattern="dd-MM-yyyy")
	@Column(name="dob")
	private LocalDate dateOfBirth;
	
	@Column(name="department",nullable = false)
	private String department;
	
	@Column(name="salary",nullable=false)
	private Double salary;
	
	@Column(name="permanent_address")
	private String permanentAddress;
	
	@Column(name="current_address")
	private String currentAddress;
	
	@Column(name="id_file_name")
	private String idProofFileName;
	
//	@Column(name="id_file_path")
//	private String idProofFilePath;
	
	@Lob
	@Column(name="id_proof")
	private byte[] idProofFile;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
