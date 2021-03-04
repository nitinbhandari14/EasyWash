package com.easywash.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easywash.model.Customer;
import com.easywash.model.Orderl;

@Component
@Service
//public interface CustomerRepository  extends CrudRepository<Customer, Long> {
	public interface OrderlRepository  extends	PagingAndSortingRepository<Orderl, Long> {

	public List<Orderl> findByofref(String mobile);
	public List<Orderl> findByofrefBetween(String start,String end);
	public Orderl findByolkey(String invoiceID);
	
	
	  public Page<Orderl> findByofref(String mobile,Pageable pageable); 
	  public  Page<Orderl> findByofrefBetween(String start,String end,Pageable pageable);
	  //public  List<Customer> findAllBycreatettsLessThanEqualAndcreatettsGreaterThanEqual(LocalDate endDate, LocalDate startDate);
	  public  List<Orderl> findBycreatettsBetween(Date start,Date end,Pageable pageable);
	  public  List<Orderl>  findBycreatettsAfter(Date start);
	 public  List<Orderl>  findBycreateusernameBetween(Date start,Date end);
	 public  List<Orderl> findBycreatettsBetween(Date start,Date end);
	 
}
