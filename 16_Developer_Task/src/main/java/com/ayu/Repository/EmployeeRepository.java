package com.ayu.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayu.Model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
