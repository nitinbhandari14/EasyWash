package com.easywash.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Data
@Entity

public class Easywashpackage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 20)
	private long Idno;
	@Column(length = 10,unique = true)
	   @Size(min=1, max=10,message="Package name must be between 2 to 10 characters")
	
	private String pname;
	@Column(length = 8)
	private String price;
	@Column(length = 8)
	private String loadkg;
	@Column(length = 4)
	 @Size(min=1, max=4,message="Days name must be between 1 to 9999")
	private String validdays;
	@Column(length = 20)
	private String createts;
	@Column(length = 20)
	private String modifyts;
	@Column(length = 20)
	private String modifyuser;
	@Column(length = 8)
	private String totalpiece;
	@Column(length = 20)
	private String rone;
	@Column(length = 20)
	private String rtwo;
	@Column(length = 20)
	private String rthree;
	@Column(length = 20)
	private String rfour;
	@Column(length = 20)
	private String rfive;
	@CreationTimestamp
	private Date testd;
	@UpdateTimestamp
    private Date updatedtest;
	public long getIdno() {
		return Idno;
	}
	public void setIdno(long idno) {
		Idno = idno;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLoadkg() {
		return loadkg;
	}
	public void setLoadkg(String loadkg) {
		this.loadkg = loadkg;
	}
	public String getValiddays() {
		return validdays;
	}
	public void setValiddays(String validdays) {
		this.validdays = validdays;
	}
	public String getCreatets() {
		return createts;
	}
	public void setCreatets(String createts) {
		this.createts = createts;
	}
	public String getModifyts() {
		return modifyts;
	}
	public void setModifyts(String modifyts) {
		this.modifyts = modifyts;
	}
	public String getModifyuser() {
		return modifyuser;
	}
	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	public String getTotalpiece() {
		return totalpiece;
	}
	public void setTotalpiece(String totalpiece) {
		this.totalpiece = totalpiece;
	}
	public String getRone() {
		return rone;
	}
	public void setRone(String rone) {
		this.rone = rone;
	}
	public String getRtwo() {
		return rtwo;
	}
	public void setRtwo(String rtwo) {
		this.rtwo = rtwo;
	}
	public String getRthree() {
		return rthree;
	}
	public void setRthree(String rthree) {
		this.rthree = rthree;
	}
	public String getRfour() {
		return rfour;
	}
	public void setRfour(String rfour) {
		this.rfour = rfour;
	}
	public String getRfive() {
		return rfive;
	}
	public void setRfive(String rfive) {
		this.rfive = rfive;
	}
	public Date getTestd() {
		return testd;
	}
	public void setTestd(Date testd) {
		this.testd = testd;
	}
	public Date getUpdatedtest() {
		return updatedtest;
	}
	public void setUpdatedtest(Date updatedtest) {
		this.updatedtest = updatedtest;
	}
	

}
