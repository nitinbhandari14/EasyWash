package com.easywash.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easywash.model.Customer;

@Component
@Service
//public interface CustomerRepository  extends CrudRepository<Customer, Long> {
	public interface CustomerRepository  extends	PagingAndSortingRepository<Customer, Long> {
	/*
	 * @Query("select u from customer u where u.mobile = ?1") List<Customer>
	 * findByMobile(String mobile);
	 */
	public List<Customer> findByMobile(String mobile);
	public List<Customer> findByckeyBetween(String start,String end);
	
	  public Page<Customer> findByMobile(String mobile,Pageable pageable); 
	  public  Page<Customer> findByckeyBetween(String start,String end,Pageable pageable);
	  //public  List<Customer> findAllBycreatettsLessThanEqualAndcreatettsGreaterThanEqual(LocalDate endDate, LocalDate startDate);
	  public  List<Customer> findBycreatettsBetween(Date start,Date end,Pageable pageable);
	  public  List<Customer>  findByref2After(Date start);
	  public  List<Customer>  findByref2Between(Date start,Date end);
	  public  List<Customer> findBycreatettsBetween(Date start,Date end);
	  public  List<Customer> findBycreatettsBetween(LocalDate start,LocalDate end);
		
	 
}
