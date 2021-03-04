package com.easywash.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.OrderlRepository;
import com.easywash.dao.OrderlinechargesRepository;
import com.easywash.dao.PackageRepository;
import com.easywash.model.Customer;
import com.easywash.model.Easywashpackage;
import com.easywash.model.LineDetails;
import com.easywash.model.Mail;
import com.easywash.model.Orderh;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.DateTimeUtil;
import com.easywash.util.EWConstants;
import com.easywash.util.RecaptchaUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.lowagie.text.DocumentException;

@RestController
public class PublicAccessController {
	  @Autowired
	    private JavaMailSender emailSender;
	@Autowired
	private CustomerRepository userRepository;
	@Autowired
	private JMSTransactionController otherController;
	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private OrderhRepository orderRepository;
	@Autowired
	private UtilController utilRepository;
	@Autowired
	private OrderlRepository orderLineRepository;
	@Autowired
	private OrderlinechargesRepository orderLineChargesRepository;
	
	  @GetMapping("/invoice/getCidOid/{cid}/{oid}") 
		public String CreateInvoice(@PathVariable("cid") Long cid,@PathVariable("oid") Long oid, Model model) throws IllegalArgumentException, Exception { //ModelAndView
			 HashMap<String, String> map = new HashMap<>();
			 
			 Optional<Customer> eachCustomer=userRepository.findById(cid); 
			 Optional<Orderh> eachOrder=orderRepository.findById(oid); 
				
				  StringBuilder returnStr=new StringBuilder();
			
				  returnStr.append("<div class='form-group'>"
				  				+"<button id='saveButton'  onclick=\"finalInvoice();\" class='btn btn-primary glyphicon glyphicon-folder-open'> Save</button>"
				  				+"<button type=\"submit\" onclick=\"saveInvoice();\" id='invoiceButton' data-dismiss=\"modal\" class='btn btn-success glyphicon glyphicon-save hidden'> Invoice</button>"
				  +"<button  onclick=\"editInvoice();\" id='editButton' class='btn btn-info glyphicon glyphicon-edit hidden'> Edit</button></div>");
				  			
				  
				//  th:text="${#dates.format(order.createtts, 'dd-MMM-yyyy HH:mm')}"
				  
				  
					
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Order#:</label> <input  value='"
					  +eachOrder.get().getIdno()+"'" +
					  "placeholder='Idno'	class='form-control'  readonly name='OIdno' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>ODate:</label> <input  value='"
					  +DateTimeUtil.formatDate(eachOrder.get().getCreatetts(),
					  EWConstants.EW_DATE_FORMAT)+"'" +
					  " placeholder='Idno'	readonly	 class='form-control' name='Orderdate' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Name:</label> <input  value='"
					  +eachCustomer.get().getFname()+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='Name' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Mob:</label> <input  value='"
					  +eachCustomer.get().getMobile()+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='Mobile' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Pack:</label> <input  value='"
					  +eachCustomer.get().getSubscribedpackage()+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='Package' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>CLoad:</label> <input  value='"
					  +String.valueOf(eachOrder.get().getWeightloadpiece())+"Kg"+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='CurrentLoad' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>PDate:</label> <input  value='"
					  +DateTimeUtil.formatDate(eachCustomer.get().getCreatetts(),
					  EWConstants.EW_DATE_FORMAT)+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='PackageDate' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Pay:</label> <input  value='"
					  +String.valueOf(eachOrder.get().getPayment())+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='PaymentType' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>CID:</label> <input  value='"
					  +String.valueOf(eachCustomer.get().getIdno())+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='Cid' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Add:</label> <input  value='"
					  +eachCustomer.get().getAddress()+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='Address' type='text'></input></div>"
					  );
					  
					  if(!Strings.isNullOrEmpty(eachCustomer.get().getLname()))
					  
					  returnStr.append("<div  class='form-group col-md-3'><label for='name'>" +
					  ":</label> <input  value='"+eachCustomer.get().getLname()+"'" +
					  "placeholder='Idno'	readonly	 class='form-control' name='RollLoad' type='text'></input></div>"
					  );
					  
					  else
					  
					  returnStr.
					  append("<div  class='form-group col-md-3'><label for='name'>RollLoad:</label> <input value='NA'"
					  +
					  "placeholder='Idno'	readonly	 class='form-control' name='RollLoad' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div  class='form-group col-md-3'><label for='name'>Notes:</label>  <input readonly id='notes' value='"
					  +eachOrder.get().getNotes()+"' class='form-control' name='Notes' type='text'></input></div>");
					  
					  returnStr.
					  append("<div id='add_more_line' class='form-group col-md-3'><label for='name'>#Lines:</label>  <input  readonly id='additionallines' value='0' class='form-control' name='AddLines' type='number'></input></div>"
					  );
					 
				
					
					  returnStr.append("<div value='0' class='form-group'>" +				
					  "					<button type='button' class=\"btn btn-default btn-info\"\r\n" +
					  "						data-dismiss=\"modal\">Close</button>"+
					  "<button id='addLine' onclick=\"addFields();\" class='btn btn-danger glyphicon glyphicon-plus'>AddLine</button>");
					  
		
			    return  returnStr.toString();
		  }
	  @PostMapping("/invoice/createi")
	  public String orderEdit(@RequestBody String data) throws DocumentException, IOException {
		 String formatData=RecaptchaUtil.decode(data);
		 String removelastChar=formatData.substring(0, formatData.length() - 1);
		 ObjectMapper objectMapper = new ObjectMapper();
		 List<Map<String, String>> jsonMap = objectMapper.readValue(removelastChar, new TypeReference<List<Map<String,String>>>(){});
		 
		 
		
		  Mail mail = new Mail(); 
		  mail.setMailTo("nitin.bhandari14@gmail.com");
		  //mail.setTemplate("Order");
		  mail.setSubject("test");
	
		//mail.setAttachments(list);
		  //mail.setProps(properties);
		  otherController.send(mail);
		 
		 
		 return parseThymeleafTemplate(jsonMap);
		 
		
		}
	  
	  private String parseThymeleafTemplate(List<Map<String, String>> jsonMap) throws DocumentException, IOException {
		    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		    templateResolver.setSuffix(".html");
		    templateResolver.setTemplateMode(TemplateMode.HTML);

		    TemplateEngine templateEngine = new TemplateEngine();
		    templateEngine.setTemplateResolver(templateResolver);

		    Context context = new Context();
		    HashMap<String, String> hmap = new HashMap<String, String>();
		    for(int i=0;i<jsonMap.size();i++)
			 {			
					  for (Map.Entry<String,String> entry : jsonMap.get(i).entrySet())
					  {
					  hmap.put(entry.getKey(), entry.getValue());
					  }
						
			 }
		   List<Easywashpackage>listPack=packageRepository.findByPnameLike(hmap.get("Package"));
		  
		    
		    Set<String> lst=hmap.keySet().stream().filter(s -> s.startsWith("lineInput")).collect(Collectors.toSet());
		    List<LineDetails> list=new ArrayList<LineDetails>();   
		    float lineTotal=0;
		    float  packageTotal=0;
		 for(int line=1;line<= lst.size()/4;line++)
		 {
			 LineDetails one=new LineDetails();
			   one.setItem_description(hmap.get("lineInputDesc"+String.valueOf(line)));
			   one.setItem_quantity(hmap.get("lineInputQty"+String.valueOf(line)));
			   one.setItem_price(hmap.get("lineInputRate"+String.valueOf(line)));
			   one.setItem_line_total(hmap.get("lineInputTotal"+String.valueOf(line)));
			   lineTotal+=Float.parseFloat(hmap.get("lineInputTotal"+String.valueOf(line)));
			   list.add(one);
						 
		 }
			//String invoiceNo=DateTimeUtil.createInvoiceKey(hmap.get("OIdno"));
		 	String invoiceNo=String.valueOf(DateTimeUtil.createUniqueKey());
		
		  context.setVariable("item_description",EWConstants.ORDER_PACKAGE_LOAD_DESC);
		   context.setVariable("item_quantity",hmap.get("CurrentLoad"));
		   context.setVariable("item_price",listPack.get(0).getTotalpiece());
		   packageTotal=Float.parseFloat(hmap.get("CurrentLoad").substring(0,hmap.get("CurrentLoad").length() -2))*(Float.parseFloat(listPack.get(0).getTotalpiece()));
		   context.setVariable("item_line_total",packageTotal);
		   context.setVariable("items",list);
		   context.setVariable("sub_total",lineTotal+packageTotal);
		   if(listPack.get(0).getPname().equalsIgnoreCase(EWConstants.CUSTOMER_DEFAULT_PACKAGE))
		   {
		   context.setVariable("amount_total",lineTotal+packageTotal);
		   createEntryinOrderLine(invoiceNo,hmap.get("OIdno"),hmap.get("Cid"),String.valueOf(lineTotal+packageTotal),EWConstants.EW_ORDER_LINE_PAYMENT_ENTRY,list,hmap.get("CurrentLoad"),listPack.get(0).getTotalpiece());
		   }
		   else
		   {
			   context.setVariable("amount_total",(lineTotal));
			
			   createEntryinOrderLine(invoiceNo,hmap.get("OIdno"),hmap.get("Cid"),String.valueOf(lineTotal),EWConstants.EW_ORDER_LINE_PAYMENT_ENTRY,list,hmap.get("CurrentLoad"),listPack.get(0).getTotalpiece());
		   }
		 
		   
			   
		    context.setVariable("Name",hmap.get("Name"));
		   context.setVariable("company_name", EWConstants.EW_COMPANY_NAME);
		   context.setVariable("company_address", EWConstants.EW_COMPANY_ADDRESS);
		   context.setVariable("company_email_web", EWConstants.EW_COMPANY_WEBSITE);
		   context.setVariable("company_phone_fax", EWConstants.EW_COMPANY_PHONE);
		   context.setVariable("issue_date_label", DateTimeUtil.getCurrentDateTime("dd-MMM-yyyy HH:mm"));
		   context.setVariable("CurrentLoad",hmap.get("CurrentLoad"));
		   
		   context.setVariable("client_name",hmap.get("Name"));
		   context.setVariable("client_address",hmap.get("Address"));
		  
		   context.setVariable("client_phone_fax",hmap.get("Mobile"));
		   Optional<Customer> singleUser=userRepository.findById(Long.parseLong(hmap.get("Cid")));
		   context.setVariable("client_city_zip_state",singleUser.get().getPincode());
		   context.setVariable("client_email",singleUser.get().getEmail());
		   context.setVariable("client_ID",hmap.get("Cid"));
		   context.setVariable("invoice_number",invoiceNo);
		   
			  
		    String returnStr=templateEngine.process("/templates/invoice_template", context);
		    generatePdfFromHtml(returnStr,hmap.get("Mobile").toString());
			  
		    return returnStr;
		}
	  private static String htmlToXhtml(String html) {
		    Document document = Jsoup.parse(html);
		    document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		   //return document.outerHtml();
		    return document.html();
		  
		}
	  @Value("${file.invoice.savepath}")
		String invoiceFile;
	  private void generatePdfFromHtml(String html,String mobile) throws DocumentException, IOException {
		    String outputFolder = invoiceFile + File.separator +  mobile+".pdf";
		    OutputStream outputStream = new FileOutputStream(outputFolder);

		    ITextRenderer renderer = new ITextRenderer();
		    
		    
		    String baseUrl = FileSystems
                    .getDefault()
                    .getPath("src", "main", "resources","static")
                    .toUri()
                    .toURL()
                    .toString();
		    
		    renderer.setDocumentFromString(htmlToXhtml(html),baseUrl);
		    renderer.layout();
		    renderer.createPDF(outputStream);
		   
		    outputStream.close();
		}
	  public void createEntryinOrderLine(String invoiceID,String orderID,String Cid,String payableAmount,String entryIdentifier,List<LineDetails> list,String packageLoad,String packprice)
	  {
		  Orderl orderPayment=new Orderl();
		  orderPayment.setOlkey(invoiceID);
			 orderPayment.setOfref(orderID);
			 orderPayment.setCref(Cid);
			 orderPayment.setItemcategory(entryIdentifier);
			 if(Float.parseFloat(payableAmount)==0.0)
			 orderPayment.setLinesubtotal(EWConstants.EW_PAYMENT_STATUS_PAID);
			 else
			 orderPayment.setLinesubtotal(EWConstants.EW_PAYMENT_STATUS_PENDG);	 
			 if(payableAmount.length()>10)
			 orderPayment.setLactive(payableAmount.substring(0,9));
			 else
				 orderPayment.setLactive(payableAmount); 
			 orderLineRepository.save(orderPayment);
			 
			 Optional<Orderh> ccc=orderRepository.findById(Long.parseLong(orderID));//.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			 ccc.get().setOref(EWConstants.ORDER_STATUS_INVOICED);
		    orderRepository.save(ccc.get());
		    //make entry in orderlinecharge table as order line entry
		    Orderlinecharges chargeEntryPack=new Orderlinecharges();
		    chargeEntryPack.setOlckey(invoiceID);//desc
		    chargeEntryPack.setReserve(EWConstants.ORDER_PACKAGE_LOAD_DESC);//desc
		    //chargeEntryPack.setCharge(packageLoad);//qty
		    chargeEntryPack.setCharge(packageLoad.substring(0,packageLoad.length()-2));//qty
		    chargeEntryPack.setRef(packprice);//rate
		    chargeEntryPack.setOlcref(orderID);//orderID
		    String packLineTotal=String.valueOf(Float.parseFloat(packageLoad.substring(0,packageLoad.length()-2))*(Float.parseFloat(packprice)));
		    if(packLineTotal.length()>9)
		    	chargeEntryPack.setReserveid(packLineTotal.substring(0,8));//lineTotal
		    else
		    	chargeEntryPack.setReserveid(packLineTotal);//lineTotal
			 
	    	
		    orderLineChargesRepository.save(chargeEntryPack);
		    for(int line=0;line< list.size();line++)
			 {
		    	LineDetails each=list.get(line);
		    	Orderlinecharges chargeEntry=new Orderlinecharges();
		    	 if(each.getItem_description().length()>39)
		    		 chargeEntry.setReserve(each.getItem_description().substring(0,39));
		    	 else
		    		 chargeEntry.setReserve(each.getItem_description());
		    	 chargeEntry.setOlckey(invoiceID);
		    	chargeEntry.setCharge(each.getItem_quantity());//qty
		    	chargeEntry.setRef(each.getItem_price());//rate
		    	chargeEntry.setOlcref(orderID);//orderID
		    	 if(each.getItem_line_total().length()>9)
		    			chargeEntry.setReserveid(each.getItem_line_total().substring(0,8));//lineTotal
				     else
		    			chargeEntry.setReserveid(each.getItem_line_total());//lineTotal
				    
		    		 chargeEntry.setReserve(each.getItem_description());
		    
		    	orderLineChargesRepository.save(chargeEntry);
							 
			 }
	  }
	  public void sendComplexMail(String[] to, String from, String subject, String text) {
		  
		  Mail mail = new Mail(); 
		  mail.setMailTo("nitin.bhandari14@gmail.com"); 
		  mail.setTemplate(text);
		  mail.setSubject("Invoice");
		  mail.setFrom("info@easywash.co.in");
		  otherController.send(mail);
		//  List<Object> list =  new ArrayList<Object>(); list.add("logo.png"); mail.setAttachments(list);
			/*
			 * 
			 * 
			 * MimeMessage message = emailSender.createMimeMessage(); MimeMessageHelper
			 * helper = null; try { helper = new MimeMessageHelper(message, true);
			 * 
			 * 
			 * helper.setFrom("info@easywash.co.in");
			 * 
			 * helper.setText(text,true);
			 * 
			 * emailSender.send(message);
			 * 
			 * 
			 * 
			 * } catch (MessagingException e) {
			 * 
			 * }
			 */
	    }
	  
	  @GetMapping("/invoice/getOid/{oid}") 
			public String getInvoices(@PathVariable("oid") Long oid, Model model) throws IllegalArgumentException, Exception { //ModelAndView
		  		StringBuilder returnStr=new StringBuilder();
				 List<Orderl> eachOrder=orderLineRepository.findByofref(String.valueOf(oid)); 
				 if(eachOrder.size()<=0)
				 {
					 returnStr.
					  append("<div class='form-group btn-danger'>NO ORDER-ID Found</div><div class='form-group col-md-3'></div>"
					  );
					 return returnStr.toString();
				 }
				 else
				 {
					
					
					  returnStr.append("<div class='form-group'></div>");
					  for(int i=0;i<eachOrder.size();i++)
					  {
						  Orderl single=eachOrder.get(i);
						  returnStr.
						  append("<div class='form-group col-md-3'><label for='name'>Order#:</label> <input  value='"
						  +single.getOfref()+"'" +
						  "placeholder='Idno'	class='form-control'  readonly name='OIdno' type='text'></input></div>"
						  );
						  returnStr.
						  append("<div class='form-group col-md-3'><label for='name'>InvoiceDate:</label> <input  value='"
						  +DateTimeUtil.formatDate(single.getCreatetts(),
						  EWConstants.EW_DATE_FORMAT)+"'" +
						  " placeholder='Idno'	readonly	 class='form-control' name='Orderdate' type='text'></input></div>"
						  );
						  returnStr.
						  append("<div class='form-group col-md-3'><label for='name'>InvoiceNumber:</label> <input  value='"
						  +single.getOlkey()+"'" +
						  "placeholder='Idno'	readonly id='"+i+"' class='form-control' name='Name' type='text'></input></div>"
						  );
						  returnStr.
						  append("<div class='form-group col-md-3'><label for='name'>Payment:</label> <input  value='"
						  +single.getLactive()+"'" +
						  "placeholder='Idno'	readonly	 class='form-control' name='Mobile' type='text'></input></div>"
						  );
						  returnStr.append("<div value='0' class='form-group'>" +		
								  "<button id='addLine'  onclick=\"sendInvoiceEmail(document.getElementById('"+i+"').value);\" class='btn btn-default btn-info glyphicon glyphicon-envelope'> Email</button>"
								  		+ "<button id='addLine'  onclick=\"downloadInvoiceEmail(document.getElementById('"+i+"').value);\" class='btn btn-default btn-warning glyphicon glyphicon-download'> Download</button></div>");
						
					  }
						  
						
						
					
				    return  returnStr.toString();
				 }
			  }
	  @GetMapping("/invoice/getIid/{Iid}") 
		public String viewInvoice(@PathVariable("Iid") Long cid, Model model) throws IllegalArgumentException, Exception { //ModelAndView
			Optional<Orderl> orderPayment= orderLineRepository.findById(cid);
			 Optional<Customer> eachCustomer=userRepository.findById(Long.valueOf(orderPayment.get().getCref())); 
			 Optional<Orderh> eachOrder=orderRepository.findById(Long.valueOf(orderPayment.get().getOfref())); 
			 List<Orderlinecharges> lines=orderLineChargesRepository.findByolckey(orderPayment.get().getOlkey());
				  StringBuilder returnStr=new StringBuilder();
			
				  returnStr.append("<div class='form-group'></div>");
					
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Order#:</label> <input  value='"
					  +eachOrder.get().getIdno()+"'" +
					  "	class='form-control'  readonly name='OIdno' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>ODate:</label> <input  value='"
					  +DateTimeUtil.formatDate(eachOrder.get().getCreatetts(),
					  EWConstants.EW_DATE_FORMAT)+"'" +
					  " 	readonly	 class='form-control' name='Orderdate' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Name:</label> <input  value='"
					  +eachCustomer.get().getFname()+"'" +
					  "	readonly	 class='form-control' name='Name' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Mob:</label> <input  value='"
					  +eachCustomer.get().getMobile()+"'" +
					  "	readonly	 class='form-control' name='Mobile' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Pack:</label> <input  value='"
					  +eachCustomer.get().getSubscribedpackage()+"'" +
					  "	readonly	 class='form-control' name='Package' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>CLoad:</label> <input  value='"
					  +String.valueOf(eachOrder.get().getWeightloadpiece())+"Kg"+"'" +
					  "	readonly	 class='form-control' name='CurrentLoad' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>PDate:</label> <input  value='"
					  +DateTimeUtil.formatDate(eachCustomer.get().getCreatetts(),
					  EWConstants.EW_DATE_FORMAT)+"'" +
					  "	readonly	 class='form-control' name='PackageDate' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Pay:</label> <input  value='"
					  +String.valueOf(eachOrder.get().getPayment())+"'" +
					  "	readonly	 class='form-control' name='PaymentType' type='text'></input></div>"
					  );
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>CID:</label> <input  value='"
					  +String.valueOf(eachCustomer.get().getIdno())+"'" +
					  "	readonly	 class='form-control' name='Cid' type='text'></input></div>"
					  );
					
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>INVKey:</label> <input  value='"
					  +String.valueOf(orderPayment.get().getOlkey())+"'" +
					  "	readonly	 class='form-control' name='InvoiceKey' type='text'></input></div>"
					  );
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>INVDate:</label> <input  value='"
					  +DateTimeUtil.formatDate(orderPayment.get().getCreatetts(),
					  EWConstants.EW_DATE_FORMAT)+"'" +
					  "	readonly	 class='form-control' name='InvoiceDate' type='text'></input></div>"
					  );
					 
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>PayINR:</label> <input  value='"
					  +String.valueOf(orderPayment.get().getLactive())+"'" +
					  "	readonly	 class='form-control' name='Payment' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div class='form-group col-md-3'><label for='name'>Add:</label> <input  value='"
					  +eachCustomer.get().getAddress()+"'" +
					  "	readonly	 class='form-control' name='Address' type='text'></input></div>"
					  );
					  
					  if(!Strings.isNullOrEmpty(eachCustomer.get().getLname()))
					  
					  returnStr.append("<div  class='form-group col-md-3'><label for='name'>" +
					  "RollLoad:</label> <input  value='"+eachCustomer.get().getLname()+"'" +
					  "	readonly	 class='form-control' name='RollLoad' type='text'></input></div>"
					  );
					  
					  else
					  
					  returnStr.
					  append("<div  class='form-group col-md-3'><label for='name'>RollLoad:</label> <input value='NA'"
					  +
					  "	readonly	 class='form-control' name='RollLoad' type='text'></input></div>"
					  );
					  
					  returnStr.
					  append("<div  class='form-group col-md-3'><label for='name'>Notes:</label>  <input readonly id='notes' value='"
					  +eachOrder.get().getNotes()+"' class='form-control' name='Notes' type='text'></input></div>");
					  
					  returnStr.
					  append("<div id='add_more_line' class='form-group col-md-3'><label for='name'>#Lines:</label>  "
					  		+ "<input  readonly id='additionallines' value='"+lines.size()+"' class='form-control' name='AddLines' type='number'></input></div>"
					  );
					  for(int i=0;i<lines.size();i++)
					  {
						  Orderlinecharges each=lines.get(i);
						 
						  returnStr.
						  append("<div class='form-group col-md-3  bg-success'><label class=\"text-primary\" for='name'>ChargeName:</label> <input  value='"
						  +String.valueOf(each.getReserve())+"'" +
						  "	readonly	 class='form-control' name='Cid' type='text'></input></div>"
						  );
						  returnStr.
						  append("<div class='form-group col-md-3 bg-warning'><label class=\"text-primary\" for='name'>Qty:</label> <input  value='"
						  +String.valueOf(each.getCharge())+"'" +
						  "	readonly	 class='form-control text-danger' name='Cid' type='text'></input></div>"
						  );
						  returnStr.
						  append("<div class='form-group col-md-3 bg-info'><label class=\"text-primary\" for='name'>Rate:</label> <input  value='"
						  +String.valueOf(each.getRef())+"'" +
						  "	readonly	 class='form-control text-danger' name='Cid' type='text'></input></div>"
						  );
						  returnStr.
						  append("<div class='form-group col-md-3 bg-danger'><label class=\"text-primary\" for='name'>LineTotal:</label> <input  value='"
						  +String.valueOf(each.getReserveid())+"'" +
						  "	readonly	 class='form-control text-danger' name='Cid' type='text'></input></div>"
						  );
					  }
					 
				
					
					  returnStr.append("<div value='0' class='form-group'>" +				
					  "					<button type='button' class=\"btn btn-default btn-info\"\r\n" +
					  "						data-dismiss=\"modal\">Close</button>");
				//	  "<button id='addLine' onclick=\"addFields();\" class='btn btn-danger glyphicon glyphicon-plus'>AddLine</button>");
					  
		
			    return  returnStr.toString();
		  }

}
