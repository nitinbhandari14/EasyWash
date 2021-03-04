package com.easywash.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@Entity

public class Customer implements java.io.Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	   @JsonProperty("Idno")
	private long  Idno;
	public long getIdno() {
		return Idno;
	}
    
@Column(length = 10) 
	public void setIdno(long idno) {
		Idno = idno;
	}

@Column(length = 20) 
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonProperty("ckey")
	private String ckey;

@Column(length = 20) 
	   @JsonProperty("fname") 
	   @Size(min=2, max=20,message="First name must be between 2 to 20 characters")
	  
	   private String fname; 

@Column(length = 20) 
	    @JsonProperty("lname")
	  private String lname; 

@Column(length = 10) 
	     @JsonProperty("subscribedpackage") 
	     private String   subscribedpackage;

@Column(length = 100) 
	      @JsonProperty("address") 
	      private String address;

@Column(length = 6) 
	   @JsonProperty("pincode")
	   private String pincode;

@Column(length = 10) 
	   @JsonProperty("mobile") 
	  
	   @Size(min=10, max=10,message="Enter only 10 digits as mobile")
	   private String mobile;

@Column(length = 50) 
	    @JsonProperty("email")	
	    
	    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email address is invalid")

		  private String email;

@Column(length = 5) 
	     @JsonProperty("active")
	     private String active;

@Column(length = 30) 
		   @JsonProperty("createts") 
		   private String createts;

@Column(length = 30) 
		  @JsonProperty("modifyts")
		  private String modifyts;

@Column(length = 20) 
		  @JsonProperty("modifyusername")
		  private String modifyusername;
		//  @Pattern(regexp="^(?=\\s*\\S).*$", message="Please select a region")

@Column(length = 30) 
			  @JsonProperty("region") private String region;
			  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
			  @JsonProperty("ref2") private Date ref2;
              
@Column(length = 30) 
			  @JsonProperty("clead") private String clead;

@Column(length = 30) 
			  @JsonProperty("createtts") 
				@CreationTimestamp
			  private Date createtts;

@Column(length = 30) 
			  @JsonProperty("modifytts") 
			  @UpdateTimestamp
			  private Date modifytts;
			  
			  
			  
			  
			 
		  
		 
	
	
			/*
			 * Customer() {}
			 * 
			 * Customer(String Key,String Fname,String Lname,String Subscribedpackage
			 * ,String Address,String Pincode,String Mobile,String Email,String
			 * Active,String Createts, String Modifyts,String Modifyusername,String
			 * Region,String Lead,String Ref2) { this.Key=Key; this.Fname=Fname;
			 * this.Lname=Lname; this.Subscribedpackage=Subscribedpackage;
			 * this.Address=Address; this.Pincode=Pincode; this.Mobile=Mobile;
			 * this.Email=Email; this.Active=Active; this.Createts=Createts;
			 * this.Modifyts=Modifyts; this.Modifyusername=Modifyusername;
			 * this.Region=Region; this.Lead=Lead; this.Ref2=Ref2;
			 * 
			 * }
			 */
	 
	

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

	public String getCkey() {
		return ckey;
	}

	public void setKey(String cckey) {
		ckey = cckey;
	}

	
	  public String getFname() { return fname; }
	  
	  public void setFname(String ffname) { fname = ffname; }
	  
	  public String getLname() { return lname; }
	  
	  public void setLname(String llname) { lname = llname; }
	  
	  public String getSubscribedpackage() { return subscribedpackage; }
	  
	  public void setSubscribedpackage(String ssubscribedpackage) {
	  subscribedpackage = ssubscribedpackage; }
	  
	  public String getAddress() { return address; }
	  
	  public void setAddress(String aaddress) { address = aaddress; }
	  
	  public String getPincode() { return pincode; }
	  
	  public void setPincode(String ppincode) { pincode = ppincode; }
	  
	  public String getMobile() { return mobile; }
	  
	  public void setMobile(String Mob) { mobile = Mob; }
	  
		
		  public String getEmail() { return email; }
		  
		  public void setEmail(String eemail) { email = eemail; }
		  
		  public String getActive() { return active; }
		  
		  public void setActive(String aactive) { active = aactive; }
		  
		  public String getCreatets() { return createts; }
		  
		  public void setCreatets(String ccreatets) { createts = ccreatets; }
		  
		  public String getModifyts() { return modifyts; }
		  
		  public void setModifyts(String mmodifyts) {
			  
			  modifyts = mmodifyts; }
		  
		  public String getModifyusername() { return modifyusername; }
		  
		  public void setModifyusername(String mmodifyusername) { modifyusername =
		  mmodifyusername; }
		  
			
			  public String getRegion() { return region; }
			  
			  public void setRegion(String rregion) { region = rregion; }
			  
			
			  
			  public Date getRef2() { return ref2; }
			  
			  public void setRef2(Date rref2) { ref2 = rref2; }
			  
			  public String getClead() { return clead; }
			  
				  public void setClead(String CClead) { clead = CClead; }
			 
		  
		 
	 
	
	
	
	
	
	

	  




}
