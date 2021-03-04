package com.easywash.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.easywash.dao.EmailSenderService;
import com.easywash.model.Mail;
import com.easywash.util.EWConstants;



@Component
@ComponentScan("com.easywash.dao")
public class JMSTransactionReceiver {
	@Autowired
    private EmailSenderService emailService;
	
	
	
	  private int count = 1;

	  @JmsListener(destination = "OrderTransactionQueue", containerFactory = "myFactory")
	  public void receiveMessage(Mail mail) throws MessagingException, IOException, javax.mail.MessagingException {
		
	    count++;
	
	
        emailService.sendEmail(mail);
      
	  }
	  @JmsListener(destination = "InvoiceTransactionQueue", containerFactory = "myFactory")
	  public void invoice(Mail mail) throws MessagingException, IOException, javax.mail.MessagingException {
		
	    count++;
	  
		/*
		 * Mail mail = new Mail(); mail.setMailTo(EWConstants.GMAIL_NITIN); //
		 * mail.setTemplate("Order"); mail.setSubject(EWConstants.EMAIL_ORDER_SUBJECT);
		 * mail.setBcc(EWConstants.EASYWASH_ARVIND);
		 * mail.setFrom(EWConstants.EASYWASH_INFO); mail.setTemplate(Emailmessage);
		 */
		 
	    List<Object> attch= mail.getAttachments();
        emailService.sendInvoiceEmail(mail,attch.get(0).toString());      
	  }

}
