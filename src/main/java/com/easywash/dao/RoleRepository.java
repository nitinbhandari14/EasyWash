package com.easywash.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.easywash.model.Role;
public interface RoleRepository extends JpaRepository<Role, Long>{
}