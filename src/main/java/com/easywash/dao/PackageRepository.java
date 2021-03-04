package com.easywash.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easywash.model.Easywashpackage;


@Component	
@Service
public interface PackageRepository extends PagingAndSortingRepository<Easywashpackage, Long> {
	public List<Easywashpackage> findByPname(String pname);
	//public List<Easywashpackage> findByPname();
	//public List<Customer> findByckeyBetween(String start,String end);
	
	public List<Easywashpackage> findByPnameLike(String pname);
	public List<Easywashpackage> findByPnameContainingIgnoreCase(String pname);
	// public Page<Easywashpackage> findByPname(String pname,Pageable pageable); 
	  //public  Page<Customer> findByckeyBetween(String start,String end,Pageable pageable);

}
