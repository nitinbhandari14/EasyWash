package com.easywash.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easywash.model.Orderh;

@Component
@Service
//public interface CustomerRepository  extends CrudRepository<Customer, Long> {
	public interface OrderhRepository  extends	PagingAndSortingRepository<Orderh, Long> {

	public List<Orderh> findBycref(String cid);
	public List<Orderh> findByokeyBetween(String start,String end);
	 public Page<Orderh> findByoref(String status,Pageable pageable);
	 public List<Orderh> findByoref(String Status);
	  public Page<Orderh> findByokey(String mobile,Pageable pageable); 
	  public  Page<Orderh> findByokeyBetween(String start,String end,Pageable pageable);
	  //public  List<Customer> findAllBycreatettsLessThanEqualAndcreatettsGreaterThanEqual(LocalDate endDate, LocalDate startDate);
	  public  List<Orderh> findBycreatettsBetween(Date start,Date end,Pageable pageable);
	  public  List<Orderh>  findBycreatettsAfter(Date start);
	 public  List<Orderh>  findBycreateusernameBetween(Date start,Date end);
	  public  List<Orderh> findBycreatettsBetween(Date start,Date end);
		
	 
}
