package com.ayu.DTO;

import org.springframework.stereotype.Component;

import com.ayu.Model.Employee;
import com.ayu.Model.User;

import lombok.Data;

@Component
@Data
public class EmployeeUserDTO {
	
	private Employee employee;
	private User user;
}
