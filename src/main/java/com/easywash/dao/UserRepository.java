package com.easywash.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywash.model.User;

//@Repository
public interface UserRepository  extends JpaRepository<User, Long>
{
	 User findByEmail(String email);
	 User findByUsername(String username);
	 
}
