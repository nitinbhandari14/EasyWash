package com.easywash.model;

import java.util.Date;
import java.util.Set;
import com.easywash.model.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
   
    @Column(length = 40)
    private String username;
    @Column(length = 250)
    private String password;
  
    @Column(length = 50)
    private String email;
    @Column(length = 50)
    private String imageUrl;
    @Column(length = 30) 
	 @JsonProperty("createtts") 
	@CreationTimestamp
	 private Date createtts;
    @Column(length = 30) 
	 @JsonProperty("modifytts") 
	 @UpdateTimestamp
	 private Date modifytts;
    @ManyToMany
    private Set<Role> roles;
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Date getCreatetts() {
		return createtts;
	}
	public void setCreatetts(Date createtts) {
		this.createtts = createtts;
	}
	public Date getModifytts() {
		return modifytts;
	}
	public void setModifytts(Date modifytts) {
		this.modifytts = modifytts;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	  

	

}
