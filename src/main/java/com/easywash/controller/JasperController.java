package com.easywash.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.OrderlRepository;
import com.easywash.dao.OrderlinechargesRepository;
import com.easywash.model.Customer;
import com.easywash.model.Mail;
import com.easywash.model.Orderh;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.DateTimeUtil;
import com.easywash.util.EWConstants;
import com.easywash.util.MediaTypeUtils;
import com.google.common.base.Strings;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@ResponseBody
@Controller
public class JasperController {
	@Autowired private CustomerRepository userRepository;
	@Autowired private OrderlRepository lineRepository;
	@Autowired private OrderlinechargesRepository orderLineChargesRepository;
	@Autowired private OrderhRepository orderRepository;
	@Autowired
	private JMSTransactionController otherController;
	  @Autowired
	    private ServletContext servletContext;
	
	@Value("${file.invoice.savepath}")
	String invoiceFile;
	
	@GetMapping(value = "/invoice/bill/{invoiceID}")
	    public String viewReport(@PathVariable("invoiceID") String cid,   ModelMap map) throws IllegalArgumentException, Exception {
		Orderl payment=lineRepository.findByolkey(cid);
		Optional<Customer> user=userRepository.findById(Long.parseLong(payment.getCref()));
		Optional<Orderh> order=orderRepository.findById(Long.parseLong(payment.getOfref()));
		createPdfReport(orderLineChargesRepository.findByolckey(cid),user.get(),order.get(),payment);
	            return "true";
	     
		
	 }
	@GetMapping(value = "/invoice/billdownload/{invoiceID}")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable("invoiceID") String cid,   ModelMap map) throws IllegalArgumentException, Exception {
	Orderl payment=lineRepository.findByolkey(cid);
	Optional<Customer> user=userRepository.findById(Long.parseLong(payment.getCref()));
	Optional<Orderh> order=orderRepository.findById(Long.parseLong(payment.getOfref()));
	return downloadPDFInvoice(orderLineChargesRepository.findByolckey(cid),user.get(),order.get(),payment);
         
     
	
 }

	 private Boolean createPdfReport(final List<Orderlinecharges>  orderLines,final  Customer cust,final Orderh orderhead,final Orderl orderpayment) throws IllegalArgumentException, Exception {
			
			   
		
		 final InputStream stream = this.getClass().getResourceAsStream("/EWInvoice.jrxml");
	       final JasperReport report = JasperCompileManager.compileReport(stream);
	        final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(orderLines);
			  final Map<String, Object> parameters = new HashMap<>();
			  parameters.put("paymentType", String.valueOf(DateTimeUtil.formatDate(orderhead.getCreatetts(), "dd MMMM yyyy")));
			  parameters.put("poNumber", orderhead.getWeightloadpiece()+"Kg");
			  parameters.put("customerEmail",orderhead.getRef1());
			  parameters.put("customerName", cust.getFname());
			  parameters.put("invoiceNumber",orderpayment.getOlkey());
			  parameters.put("invoiceDate", String.valueOf(DateTimeUtil.formatDate(orderpayment.getCreatetts(), "dd MMMM yyyy")));
			  parameters.put("invoiceAmount",  orderpayment.getLactive());
			  parameters.put("logo",ClassLoader.getSystemResourceAsStream("logo.png") );
		final JasperPrint print = JasperFillManager.fillReport(report,parameters,  source);
	     final String filePath = invoiceFile+ File.separator +orderpayment.getOlkey()+".pdf";
	         JasperExportManager.exportReportToPdfFile(print,filePath);
	         InvoiceEmail(filePath,cust.getEmail(),cust.getFname(),orderhead.getIdno(),orderpayment.getOlkey(),orderhead.getWeightloadpiece());
	        return true;
	    }
	 
	 private void InvoiceEmail(String filepath,String emailID,String name,Long order,String invoice,String load)
	 {
		  Mail mail = new Mail(); 
		  if(Strings.isNullOrEmpty(emailID))
			  mail.setMailTo(EWConstants.GMAIL_ARVIND);
		  else
		  mail.setMailTo(emailID); 
		  
		  mail.setTemplate("Invoice");
		  mail.setSubject("Invoice");
		  
		  Map<String, Object> properties = new
		  HashMap<String, Object>();
		  properties.put("name", name);
		  properties.put("location", "d");
		  properties.put("currentWeight",load); 
		  properties.put("orderNo", String.valueOf(order));
		  properties.put("sPack", invoice);
		  List<Object> list =  new ArrayList<Object>();
		  list.add(filepath);
		  mail.setAttachments(list);
		  mail.setProps(properties); 
		  otherController.sendInvoice(mail);
	 }
	 
	 private ResponseEntity<InputStreamResource> downloadPDFInvoice(final List<Orderlinecharges>  orderLines,final  Customer cust,final Orderh orderhead,final Orderl orderpayment) throws IllegalArgumentException, Exception {
			
		   
			
		 final InputStream stream = this.getClass().getResourceAsStream("/EWInvoice.jrxml");
	       final JasperReport report = JasperCompileManager.compileReport(stream);
	        final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(orderLines);
			  final Map<String, Object> parameters = new HashMap<>();
			  parameters.put("paymentType", String.valueOf(DateTimeUtil.formatDate(orderhead.getCreatetts(), "dd MMMM yyyy")));
			  parameters.put("poNumber", orderhead.getWeightloadpiece()+"Kg");
			  parameters.put("customerEmail",orderhead.getRef1());
			  parameters.put("customerName", cust.getFname());
			  parameters.put("invoiceNumber",orderpayment.getOlkey());
			  parameters.put("invoiceDate", String.valueOf(DateTimeUtil.formatDate(orderpayment.getCreatetts(), "dd MMMM yyyy")));
			  parameters.put("invoiceAmount",  orderpayment.getLactive());
			  parameters.put("logo",ClassLoader.getSystemResourceAsStream("logo.png") );
		final JasperPrint print = JasperFillManager.fillReport(report,parameters,  source);
	     final String filePath = invoiceFile+ File.separator +orderpayment.getOlkey()+".pdf";
	         JasperExportManager.exportReportToPdfFile(print,filePath);
	        return  downloadInvoice(filePath,orderpayment.getOlkey()+".pdf");
	       
	    }
	   public ResponseEntity<InputStreamResource> downloadInvoice(
	             String CompleteFilePath,String fileName) throws IOException {
	 
	        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
	      
	        File file = new File(CompleteFilePath);
	        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
	 
	        return ResponseEntity.ok()
	                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
	                .contentType(mediaType)
	                 .contentLength(file.length())
	                .body(resource);
	    }

}
