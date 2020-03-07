package com.apptime.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.apptime.auth.model.Users;
/**
 * @author Bashiir Mohamed
 * this class represent jpa user repository
 */
public interface UserRepository extends JpaRepository<Users, Integer>{
	Users findByUsername(String username);
	//Users findByUser_id(int userId);
	
}
