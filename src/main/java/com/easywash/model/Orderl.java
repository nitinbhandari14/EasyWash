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

public class Orderl implements java.io.Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	   @JsonProperty("Idno")
    

	private long  Idno;
//invoicekey
	@Column(length = 20)
		private String olkey;
	
	@Column(length = 200)
	   @JsonProperty("notes") 
	   private String notes; 
//order reference
 @Column(length = 10)
	    @JsonProperty("ofref")
	  private String ofref; 
//customer reference	    
	    @Column(length = 10)  
	     @JsonProperty("cref") 
	     private String   cref;
	      
	    @Column(length = 10)  
	      @JsonProperty("pref") 
	      private String pref;
	      
	      @Column(length = 10)  
	   @JsonProperty("refkey")
	   private String refkey;
	 
	    //payable amount
	    @Column(length = 10)
	     @JsonProperty("lactive")
	     private String lactive;
	     
	 	@Column(length = 40)
		   @JsonProperty("validdate") 
		   private String validdate;
		
			@Column(length = 5)
		  @JsonProperty("unitprice")
		  private String unitprice;
		  //payment paid or pending
			@Column(length = 5)
			  @JsonProperty("linesubtotal")
			  private String linesubtotal;
			@Column(length = 10)
			  @JsonProperty("lineref")
			  private String lineref;
			//order line order shop charges
			@Column(length = 5)
			  @JsonProperty("itemcategory")
			  private String itemcategory;
			//item description 
			@Column(length = 40)
			  @JsonProperty("itemid")
			  private String itemid;
			//prime line no
			@Column(length = 5)
			  @JsonProperty("primelineno")
			  private String primelineno;
			@Column(length = 10)
			  @JsonProperty("reserveid")
			  private String reserveid;
			//qty
			@Column(length = 5)
		  @JsonProperty("weightloadpiece")
		  private String weightloadpiece;
			
		  		@Column(length = 20)
			  @JsonProperty("linetotal") 
			  private String linetotal;
		  	  @Column(length = 30)
			  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
			  @JsonProperty("ref2") 
			  private Date ref2;
		  	//transaction id
			  @Column(length = 120)
			  @JsonProperty("ref1") 
			  private String ref1;
			  //payment reference
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

			public long getIdno() {
				return Idno;
			}

			public void setIdno(long idno) {
				Idno = idno;
			}

			public String getOlkey() {
				return olkey;
			}

			public void setOlkey(String olkey) {
				this.olkey = olkey;
			}

			public String getNotes() {
				return notes;
			}

			public void setNotes(String notes) {
				this.notes = notes;
			}

			public String getOfref() {
				return ofref;
			}

			public void setOfref(String ofref) {
				this.ofref = ofref;
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

			public String getLactive() {
				return lactive;
			}

			public void setLactive(String lactive) {
				this.lactive = lactive;
			}

			public String getValiddate() {
				return validdate;
			}

			public void setValiddate(String validdate) {
				this.validdate = validdate;
			}

			public String getUnitprice() {
				return unitprice;
			}

			public void setUnitprice(String unitprice) {
				this.unitprice = unitprice;
			}

			public String getLinesubtotal() {
				return linesubtotal;
			}

			public void setLinesubtotal(String linesubtotal) {
				this.linesubtotal = linesubtotal;
			}

			public String getLineref() {
				return lineref;
			}

			public void setLineref(String lineref) {
				this.lineref = lineref;
			}

			public String getItemcategory() {
				return itemcategory;
			}

			public void setItemcategory(String itemcategory) {
				this.itemcategory = itemcategory;
			}

			public String getItemid() {
				return itemid;
			}

			public void setItemid(String itemid) {
				this.itemid = itemid;
			}

			public String getPrimelineno() {
				return primelineno;
			}

			public void setPrimelineno(String primelineno) {
				this.primelineno = primelineno;
			}

			public String getReserveid() {
				return reserveid;
			}

			public void setReserveid(String reserveid) {
				this.reserveid = reserveid;
			}

			public String getWeightloadpiece() {
				return weightloadpiece;
			}

			public void setWeightloadpiece(String weightloadpiece) {
				this.weightloadpiece = weightloadpiece;
			}

			public String getLinetotal() {
				return linetotal;
			}

			public void setLinetotal(String linetotal) {
				this.linetotal = linetotal;
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

			public static long getSerialversionuid() {
				return serialVersionUID;
			}

	
}
