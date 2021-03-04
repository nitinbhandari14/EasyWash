package com.easywash.model;



import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    @Column(length = 50)
    private String name;
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
   
    
	  

	

}
