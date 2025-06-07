package com.ayu.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayu.Model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	List<Employee> findAllByOrderByFirstNameDesc();
	List<Employee> findAllByOrderBySalaryDesc();
	Employee findByEmail(String email);
	
}
