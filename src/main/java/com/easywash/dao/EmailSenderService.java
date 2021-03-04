package com.easywash.dao;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.easywash.model.Mail;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    public void sendEmail(Mail mail) throws MessagingException, IOException, javax.mail.MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
       
              //  helper.addAttachment("Attachment.png", new ClassPathResource((String)mail.getAttachments().get(0)));
       
        	
        Context context = new Context();
        context.setVariables(mail.getProps());
        String html = templateEngine.process(mail.getTemplate(), context);
        //String html = templateEngine.process("newsletter-template", context);
        helper.setTo(mail.getMailTo());
        helper.setBcc(mail.getBcc());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        emailSender.send(message);
    }
    public void sendInvoiceEmail(Mail mail,String path) throws MessagingException, IOException, javax.mail.MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
       
              //  helper.addAttachment("Attachment.png", new ClassPathResource((String)mail.getAttachments().get(0)));
        FileSystemResource file = new FileSystemResource(new File(path));
        
        helper.addAttachment("Invoice.pdf", file);
        Context context = new Context();
        context.setVariables(mail.getProps());
        String html = templateEngine.process(mail.getTemplate(), context);
        //String html = templateEngine.process("newsletter-template", context);
        helper.setTo(mail.getMailTo());
        helper.setBcc(mail.getBcc());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        emailSender.send(message);
    }
}
