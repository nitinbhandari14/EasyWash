package com.easywash.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.OrderlRepository;
import com.easywash.dao.OrderlinechargesRepository;
import com.easywash.model.Customer;
import com.easywash.model.Orderh;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.DateTimeUtil;
import com.easywash.util.EWConstants;
import com.easywash.util.MediaTypeUtils;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ReportController {
	@Autowired private CustomerRepository userRepository;
	@Autowired private OrderlRepository lineRepository;
	@Autowired private OrderlinechargesRepository orderLineChargesRepository;
	@Autowired private OrderhRepository orderRepository;
	@Autowired private JMSTransactionController otherController;
	
	  	@Value("${file.invoice.savepath}")
	String invoiceFile;
	    private static final String DEFAULT_FILE_NAME = "Invoice.pdf";
	    
	   
	    @Autowired
	    private ServletContext servletContext;

		/*
		 * @RequestMapping("report/MonthlyDownload") public
		 * ResponseEntity<InputStreamResource> downloadFile1(
		 * 
		 * @RequestParam(defaultValue = DEFAULT_FILE_NAME) String fileName) throws
		 * IOException {
		 * 
		 * MediaType mediaType =
		 * MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
		 * 
		 * File file = new File(invoiceFile+ "/" + fileName); InputStreamResource
		 * resource = new InputStreamResource(new FileInputStream(file));
		 * 
		 * return ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION,
		 * "attachment;filename=" + file.getName()) .contentType(mediaType)
		 * .contentLength(file.length()) .body(resource); }
		 */
		    @GetMapping(value = "/report/daily/{date}")
		    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start) throws IllegalArgumentException, Exception {
		    	String endDate=DateTimeUtil.getCurrentDateTime(EWConstants.MP_OMS_DATE_FORMAT);
			List<Orderl> payment=lineRepository.findBycreatettsBetween(start,DateTimeUtil.convertDate(endDate));
			List<Customer> user=userRepository.findBycreatettsBetween(start,DateTimeUtil.convertDate(endDate));
			List<Orderh> order=orderRepository.findBycreatettsBetween(start,DateTimeUtil.convertDate(endDate));
			List<Orderlinecharges> orderlinecharges=orderLineChargesRepository.findBycreatettsBetween(start,DateTimeUtil.convertDate(endDate));
			return downloadReport(payment,user,order,orderlinecharges,String.valueOf(start));
		 }
		    
		    private ResponseEntity<InputStreamResource> downloadReport(final List<Orderl> payment,final  List<Customer> user,final	List<Orderh> order,List<Orderlinecharges> orderlinecharges,final String filename) throws IllegalArgumentException, Exception {
				 final InputStream stream = this.getClass().getResourceAsStream("/DailyReport.jrxml");
			       final JasperReport report = JasperCompileManager.compileReport(stream);
			        final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(order);
					  final Map<String, Object> parameters = new HashMap<>();
					  parameters.put("Date:", " ");
					  parameters.put("Report Name:"," ");
					  parameters.put("Initiated By:"," ");
					  parameters.put("Company ", "");
					  parameters.put("Notes ", "");
				final JasperPrint print = JasperFillManager.fillReport(report,parameters,  source);
			     final String filePath = invoiceFile+ File.separator +"Report"+".pdf";
			         JasperExportManager.exportReportToPdfFile(print,filePath);
			        return  downloadInvoice(filePath,filename+".pdf");
			       
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
			   //TODO:Work on search with Date
			   @ResponseBody
			    @GetMapping(value = "/test/{start}/{end}")
			    public List<Customer> test(@PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")  LocalDate start,@PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")  LocalDate end) throws IllegalArgumentException, Exception {
			    	String endDate=end+" 16:39:41.149000";
			    	String startDate=start+" 16:39:41.149000";
			    	Date test=DateTimeUtil.convertDate(startDate, "yyyy-MM-dd hh:mm:ss");
				// List<Customer> lines=userRepository.findBycreatettsBetween(DateTimeUtil.convertDate(startDate),DateTimeUtil.convertDate(endDate));
			    	List<Customer> lines=userRepository.findBycreatettsBetween(start,end);
					
				 return lines;
			    }


			

}
