package com.easywash.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.easywash.dao.RoleRepository;
import com.easywash.dao.UserRepository;
import com.easywash.model.User;

@Service
public class UserService {
	 @Autowired
	    private UserRepository userRepository;
	    @Autowired
	    private RoleRepository roleRepository;
	    @Autowired
	    private BCryptPasswordEncoder bCryptPasswordEncoder;
	    
	    public void save(User user) {
	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	       // user.setUsername(user.getName());
	        user.setRoles(new HashSet<>(roleRepository.findAll()));
	        userRepository.save(user);
	    }
	    
	    public User findByUsername(String username) {
	        return userRepository.findByUsername(username);
	    }


}
