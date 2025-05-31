package com.ayu.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ayu.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("select u from User u where u.username = :username")
	User getByUserName(@Param("username")String username);
}
