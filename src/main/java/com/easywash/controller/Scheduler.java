package com.easywash.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.easywash.dao.CustomerRepository;
import com.easywash.model.Customer;
import com.easywash.model.Mail;
import com.easywash.util.EWConstants;

@Component
public class Scheduler {
	@Autowired
	private CustomerRepository userRepository;
	
	@Scheduled(cron= "0 0/50 7-19 * * *")
	public void run() {
		Calendar c = Calendar.getInstance();
    	c.setTime(new Date());
    	c.add(Calendar.HOUR, 6);
    	Date newDate = c.getTime();
		List<Customer>l2=userRepository.findByref2Between(new Date(),newDate); 
	    for(Customer cc:l2)
	    {
	    	Mail mail = new Mail();
			mail.setBcc(EWConstants.GMAIL_ARVIND);
			mail.setFrom(EWConstants.EASYWASH_INFO);
			mail.setMailTo(EWConstants.EASYWASH_NITIN);
			mail.setTemplate("emailUserReg");
			mail.setSubject("NewUserRegistration");
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("name", "lead");
			properties.put("location","lead");
			properties.put("sign", "lead");
			properties.put("orderNo", "lead");
			properties.put("Amount", "lead");
			List<Object> list = new ArrayList<Object>();
			list.add("logo.png");
			mail.setAttachments(list);
			mail.setProps(properties);
			//otherController.send(mail);
	    
	    	 System.out.println("Current time is :: " + Calendar.getInstance().getTime()+" "+cc.getIdno()+cc.getRef2()+newDate);
	    }
	
	}
}
