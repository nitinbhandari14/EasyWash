package com.easywash.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.easywash.dao.CustomerRepository;
import com.easywash.model.Customer;
import com.easywash.model.Donto;
import com.easywash.model.Ponto;
import com.easywash.util.DateTimeUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Controller

public class CanvasjsChartController {
	@Autowired
	private CustomerRepository userRepository;

	SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
	///
	@GetMapping(path = "/customer/chart")
	public String getDataPlot(ModelMap model) throws IllegalArgumentException, Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		   
		   
		//   System.out.println(dtf.format(now));
		
		PageRequest pageable = PageRequest.of(0, 10000, Sort.by("ckey").ascending());
		List<Customer> list = userRepository.findBycreatettsBetween(DateTimeUtil.convertDate(dtf.format(LocalDateTime.now().minusMonths(6)), "yyyy-MM-dd"),
				DateTimeUtil.convertDate(dtf.format(LocalDateTime.now().plusDays(1)), "yyyy-MM-dd"), pageable);
		
		
		Multimap<Object, Object> multiMap1 = ArrayListMultimap.create();
		Multimap<Object, Object> multiMap2 = ArrayListMultimap.create();
		
		
		if (list.size() > 0) {
			for (Customer each : list) {
				Date dd = each.getCreatetts();
				Long lo=DateTimeUtil.stringDateToEPOCHTimebased(DateTimeUtil.formatOnlyDate(dd,"yyyy-MM-dd")+" 10:10:10","yyyy-MM-dd HH:mm:ss");
				if (each.getActive().equals("A")) {
					Object key=lo;
						multiMap1.put(key, each.getIdno());
				} else {
					Object key=lo;
					multiMap2.put(key, each.getIdno());
				}
			}
			
		}
		
			Map<Object,Collection<Object>> collect=multiMap1.asMap();
			SortedSet<Object> key=new TreeSet<Object>(collect.keySet());
			List<Ponto> pontos = new ArrayList<>();
			for(Object peach:key)
			{
				Ponto ponto=new Ponto();
				ponto.setX(peach);
				ponto.setY(collect.get(peach).size());
				pontos.add(ponto);
				
			}
			
			Map<Object,Collection<Object>> collect2=multiMap2.asMap();
			SortedSet<Object> key2=new TreeSet<Object>(collect2.keySet());
			List<Donto> dontos = new ArrayList<>();
			
			for(Object deach2:key2)
			{
				Donto donto=new Donto();
				donto.setX(deach2);
				donto.setY(collect2.get(deach2).size());
				dontos.add(donto);
				
			}
			
	 
			model.addAttribute("don", dontos);
			model.addAttribute("pon", pontos);
		
			return "cChart";
	}

	@ResponseBody
	@GetMapping("/customer/pchartdate/{fromDate}/{toDate}")
	public List<Customer> getCustomersearchBydate(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, Model model, RedirectAttributes atts)
			throws IllegalArgumentException, Exception {

		PageRequest pageable = PageRequest.of(0, 10000, Sort.by("createtts").descending());
		return userRepository.findBycreatettsBetween(DateTimeUtil.convertDate(from, "yyyy-MM-dd"),
				DateTimeUtil.convertDate(to, "yyyy-MM-dd"), pageable);
	}

	//@ResponseBody
	@GetMapping("/customer/pchartdatesearch/{fromDate}/{toDate}")
	public String getCustomerChartsearchBydate(@PathVariable("fromDate") String from, @PathVariable("toDate") String to,
			ModelMap model, RedirectAttributes atts) throws IllegalArgumentException, Exception {
		PageRequest pageable = PageRequest.of(0, 10000, Sort.by("ckey").ascending());
		List<Customer> list = userRepository.findBycreatettsBetween(DateTimeUtil.convertDate(from, "yyyy-MM-dd"),
				DateTimeUtil.convertDate(to, "yyyy-MM-dd"), pageable);
		
		
		Multimap<Object, Object> multiMap1 = ArrayListMultimap.create();
		Multimap<Object, Object> multiMap2 = ArrayListMultimap.create();
		
		
		if (list.size() > 0) {
			for (Customer each : list) {
				Date dd = each.getCreatetts();
				Long lo=DateTimeUtil.stringDateToEPOCHTimebased(DateTimeUtil.formatOnlyDate(dd,"yyyy-MM-dd")+" 10:10:10","yyyy-MM-dd HH:mm:ss");
				if (each.getActive().equals("A")) {
					Object key=lo;
						multiMap1.put(key, each.getIdno());
				} else {
					Object key=lo;
					multiMap2.put(key, each.getIdno());
				}
			}
			
		}
		
			Map<Object,Collection<Object>> collect=multiMap1.asMap();
			SortedSet<Object> key=new TreeSet<Object>(collect.keySet());
			List<Ponto> pontos = new ArrayList<>();
			for(Object peach:key)
			{
				Ponto ponto=new Ponto();
				ponto.setX(peach);
				ponto.setY(collect.get(peach).size());
				pontos.add(ponto);
				
			}
			
			Map<Object,Collection<Object>> collect2=multiMap2.asMap();
			SortedSet<Object> key2=new TreeSet<Object>(collect2.keySet());
			List<Donto> dontos = new ArrayList<>();
			
			for(Object deach2:key2)
			{
				Donto donto=new Donto();
				donto.setX(deach2);
				donto.setY(collect2.get(deach2).size());
				dontos.add(donto);
				
			}
			
	 
			model.addAttribute("don", dontos);
			model.addAttribute("pon", pontos);
		
			return "cChart";
	

	}

}

