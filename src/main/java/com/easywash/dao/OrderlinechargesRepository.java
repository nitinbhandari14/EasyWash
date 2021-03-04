package com.easywash.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easywash.model.Orderlinecharges;

@Component
@Service
//public interface CustomerRepository  extends CrudRepository<Customer, Long> {
	public interface OrderlinechargesRepository  extends	PagingAndSortingRepository<Orderlinecharges, Long> {

	
	  public List<Orderlinecharges> findByreserve(String mobile); 
	  public List<Orderlinecharges>  findByreserveidBetween(String start,String end);
	  public Page<Orderlinecharges> findByreserve(String mobile,Pageable pageable);
	  public Page<Orderlinecharges> findByreserveidBetween(String start,String end,Pageable pageable); //public List<Customer>
	 // findAllBycreatettsLessThanEqualAndcreatettsGreaterThanEqual(LocalDate  endDate, LocalDate startDate);
	  public List<Orderlinecharges> findBycreatettsBetween(Date start,Date end,Pageable pageable);
	  public List<Orderlinecharges> findBycreatettsAfter(Date start); 
	  public List<Orderlinecharges> findBycreateusernameBetween(Date start,Date end);
	  public List<Orderlinecharges> findByolckey(String mobile);
	  public Page<Orderlinecharges> findBychargename(String mobile,Pageable pageable);
	  public List<Orderlinecharges> findBycreatettsBetween(Date start,Date end);
		
	  
	//  public List<Orderlinecharges>  findByreserveidAndreserve(String amount,String name);
	//  public Page<Orderlinecharges> findBycreatettsBetweenAndreservein(Date start,Date end,String reserve,Pageable pageable);
		
		
	 	
		 
	  
		 
	 
}
