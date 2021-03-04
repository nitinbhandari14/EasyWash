package com.easywash.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@Entity

public class Orderh implements java.io.Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	   @JsonProperty("Idno")
                      
  	private long  Idno;

	@Column(length = 20)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonProperty("okey")
	private String okey;
	//Wash Instructions
	@Column(length = 200)
	   @JsonProperty("notes") 
	   private String notes; 
//Status of Order
 @Column(length = 10)
	    @JsonProperty("oref")
	  private String oref; 
 
 @Column(length = 10)
 @JsonProperty("enterprisek")
private String enterprisek; 
	    //customerID
	    @Column(length = 10)  
	     @JsonProperty("cref") 
	     private String   cref;
	     //subscribed package 
	    @Column(length = 10)  
	      @JsonProperty("pref") 
	      private String pref;
	      //it will store payment key
	      @Column(length = 10)  
	   @JsonProperty("refkey")
	   private String refkey;
	 
	    
	    @Column(length = 10)
	     @JsonProperty("oactive")
	     private String oactive;
	     
	 	@Column(length = 40)
		   @JsonProperty("validdate") 
		   private String validdate;
		   
			@Column(length = 10)
		  @JsonProperty("count")
		  private String count;
		  //Current Load
			
			@Column(length = 40)
		  @JsonProperty("weightloadpiece")
		  private String weightloadpiece;
	//Prepaid Or Postpaid
		  		@Column(length = 20)
			  @JsonProperty("payment") 
			  private String payment;
		  	  @Column(length = 30)
			  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
			  @JsonProperty("ref2") 
			  private Date ref2;
			  //address
			  @Column(length = 120)
			  @JsonProperty("ref1") 
			  private String ref1;
			  
			  @Column(length = 120)
			  @JsonProperty("ref3") 
			  private String ref3;
			 
			  @JsonProperty("createtts") 
				@CreationTimestamp
			  private Date createtts;
			  
			  @JsonProperty("modifytts") 
			  @UpdateTimestamp
			  private Date modifytts;
			  
			  @Column(length = 40)
			  @JsonProperty("modifyusername")
			  private String modifyusername;
			  
			  @Column(length = 40)
			  @JsonProperty("createusername")
			  private String createusername;

			public String getEnterprisek() {
				return enterprisek;
			}

			public void setEnterprisek(String enterprisek) {
				this.enterprisek = enterprisek;
			}

			public String getModifyusername() {
				return modifyusername;
			}

			public void setModifyusername(String modifyusername) {
				this.modifyusername = modifyusername;
			}

			public String getCreateusername() {
				return createusername;
			}

			public void setCreateusername(String createusername) {
				this.createusername = createusername;
			}

			public long getIdno() {
				return Idno;
			}

			public void setIdno(long idno) {
				Idno = idno;
			}

			public String getOkey() {
				return okey;
			}

			public void setOkey(String okey) {
				this.okey = okey;
			}

			public String getNotes() {
				return notes;
			}

			public void setNotes(String notes) {
				this.notes = notes;
			}

			public String getOref() {
				return oref;
			}

			public void setOref(String oref) {
				this.oref = oref;
			}

			public String getCref() {
				return cref;
			}

			public void setCref(String cref) {
				this.cref = cref;
			}

			public String getPref() {
				return pref;
			}

			public void setPref(String pref) {
				this.pref = pref;
			}

			public String getRefkey() {
				return refkey;
			}

			public void setRefkey(String refkey) {
				this.refkey = refkey;
			}

			public String getOactive() {
				return oactive;
			}

			public void setOactive(String oactive) {
				this.oactive = oactive;
			}

			public String getValiddate() {
				return validdate;
			}

			public void setValiddate(String validdate) {
				this.validdate = validdate;
			}

			public String getCount() {
				return count;
			}

			public void setCount(String count) {
				this.count = count;
			}

			public String getWeightloadpiece() {
				return weightloadpiece;
			}

			public void setWeightloadpiece(String weightloadpiece) {
				this.weightloadpiece = weightloadpiece;
			}

			public String getPayment() {
				return payment;
			}

			public void setPayment(String payment) {
				this.payment = payment;
			}

			public Date getRef2() {
				return ref2;
			}

			public void setRef2(Date ref2) {
				this.ref2 = ref2;
			}

			public String getRef1() {
				return ref1;
			}

			public void setRef1(String ref1) {
				this.ref1 = ref1;
			}

			public String getRef3() {
				return ref3;
			}

			public void setRef3(String ref3) {
				this.ref3 = ref3;
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

			public static long getSerialversionuid() {
				return serialVersionUID;
			}
			  
			  
			  
			  
			 
		  
		 
	
	


}
