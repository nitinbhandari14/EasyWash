package com.easywash.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@Entity

public class Orderlinecharges implements java.io.Serializable {
	
	
	
	  private static final long serialVersionUID = 1L;
	  
	  @Id
	  
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  
	  
	  private long Idno;
	  
	//invoicekey
	  @Column(length = 20)
	  
	  private String olckey;
	  
	  //
	  @Column(length = 10)
	  
	  private String olcref;
	  
	  
	  @Column(length = 10)
	  
	  private String categoryname;
	  
	  @Column(length = 10)
	  
	   private String chargename;
	  
	  //qty
	  @Column(length = 5)	  
	 private String charge;
	  //rate	  
	  @Column(length = 5)	  
	 private String ref;
	  
	  @Column(length = 5)
	  
	  private String dorc;
	  //Item Desc
	  @Column(length = 40)
	  
	 private String reserve;
	  
	  //line total
	  @Column(length = 10)
	  
	 private String reserveid;
	  
	  
	 
	  @CreationTimestamp private Date createtts;
	  
	  
	  @UpdateTimestamp private Date modifytts;
	  
	  @Column(length = 40)
	  
	 private String modifyusername;
	  
	  @Column(length = 40)
	  
	  private String createusername;
	  
	  public long getIdno() { return Idno; }
	  
	  public void setIdno(long idno) { Idno = idno; }
	  
	  public String getOlckey() { return olckey; }
	  
	  public void setOlckey(String olckey) { this.olckey = olckey; }
	  
	  public String getOlcref() { return olcref; }
	  
	  public void setOlcref(String olcref) { this.olcref = olcref; }
	  
	  public String getCategoryname() { return categoryname; }
	  
	  public void setCategoryname(String categoryname) { this.categoryname =
	  categoryname; }
	  
	  public String getChargename() { return chargename; }
	  
	  public void setChargename(String chargename) { this.chargename = chargename;
	  }
	  
	  public String getCharge() { return charge; }
	  
	  public void setCharge(String charge) { this.charge = charge; }
	  
	  public String getRef() { return ref; }
	  
	  public void setRef(String ref) { this.ref = ref; }
	  
	  public String getDorc() { return dorc; }
	  
	  public void setDorc(String dorc) { this.dorc = dorc; }
	  
	  public String getReserve() { return reserve; }
	  
	  public void setReserve(String reserve) { this.reserve = reserve; }
	  
	  public String getReserveid() { return reserveid; }
	  
	  public void setReserveid(String reserveid) { this.reserveid = reserveid; }
	  
	  public Date getCreatetts() { return createtts; }
	  
	  public void setCreatetts(Date createtts) { this.createtts = createtts; }
	  
	  public Date getModifytts() { return modifytts; }
	  
	  public void setModifytts(Date modifytts) { this.modifytts = modifytts; }
	  
	  public String getModifyusername() { return modifyusername; }
	  
	  public void setModifyusername(String modifyusername) { this.modifyusername =
	  modifyusername; }
	  
	  public String getCreateusername() { return createusername; }
	  
	  public void setCreateusername(String createusername) { this.createusername =
	  createusername; }
	  
	  

	
}
