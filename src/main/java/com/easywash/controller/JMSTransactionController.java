package com.easywash.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easywash.model.Mail;
import com.easywash.util.EWConstants;

@RestController
@RequestMapping("/jms")
public class JMSTransactionController {
	
	 

  @Autowired private JmsTemplate jmsTemplate;
  @PostMapping("/email")
  public void send(@RequestBody Mail transaction) {
    // Post message to the message queue named "OrderTransactionQueue"
	  transaction.setBcc(EWConstants.EASYWASH_ARVIND);
	  transaction.setFrom(EWConstants.EASYWASH_INFO);
    jmsTemplate.convertAndSend("OrderTransactionQueue", transaction);
  }
  public void sendInvoice(@RequestBody Mail transaction) {
	  transaction.setBcc(EWConstants.EASYWASH_ARVIND);
	  transaction.setFrom(EWConstants.EASYWASH_INFO);
  
	  jmsTemplate.convertAndSend("InvoiceTransactionQueue",transaction);
  	}
 
  }
