package com.ayu.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ayu.DTO.EmployeeUserDTO;
import com.ayu.Model.Employee;
import com.ayu.Model.User;
import com.ayu.Repository.EmployeeRepository;
import com.ayu.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository empRepo;
	
	@Autowired
	UserRepository userRepo;
	
	 /**
     * Save employee along with user (if HR).
     * Returns the saved Employee entity.
     */
	@Transactional
	public Employee saveEmployeeWithUser(EmployeeUserDTO dto)
	{
		if(dto == null || dto.getEmployee() == null)
		{
			throw new IllegalArgumentException("Employee data must not be null");
		}
		
		
		Employee emp = dto.getEmployee();
		User user = dto.getUser();
		
		
		if("HR".equalsIgnoreCase(emp.getDepartment()))
		{
			//Generate username and set user fields
			String username = generateUsername(emp.getFirstName(),emp.getLastName());
			user.setUsername(username);
			user.setRole("HR");
			user.setActive(true);
			user.setCreatedAt(LocalDateTime.now());
			
			
			//Save user
			userRepo.save(user);
			
			//Link user to employee
			emp.setUser(user);
			
			//Generate login ID using UserID
			emp.setEmpId(generateEmpId());
			
		}else {
			//Non-HR employee: loginID not required
			emp.setUser(null);
			emp.setEmpId(generateEmpId());
			
		}
		
		
		// Save and return employee
		return empRepo.save(emp);
		
	}

		
	/**
     * Generate login ID based on user ID.
     */
	private String generateEmpId() {
		long count = empRepo.count()+1;
		return String.format("EMP%03d", count);
	}
	
	 /**
     * Generate a username using first and last name + random digits.
     */
	private String generateUsername(String firstName, String lastName) {
		String base = (firstName.charAt(0)+"_"+lastName).toLowerCase();
		int random = new Random().nextInt(900) + 100; // 100-900
		return base + random;
	}
	
	/**
	 * Sort Employee details on the basis of their name
	 */
	@Transactional
	public List<Employee> findAllOrderByNameAsc()
	{
		return empRepo.findAllByOrderByFirstNameDesc();
	}
	@Transactional
	public List<Employee> findAllOrderSalaryAsc()
	{
		return empRepo.findAllByOrderBySalaryDesc();
	}
	
	public Employee findByEmailID(String email)
	{
		return empRepo.findByEmail(email);
	}
	
	/**
	 * Pagination Logic
	 * */
	
	
}
