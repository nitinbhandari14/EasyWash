package com.easywash.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.PackageRepository;
import com.easywash.model.Customer;
import com.easywash.model.Easywashpackage;
import com.easywash.model.Mail;
import com.easywash.model.Orderh;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.DateTimeUtil;
import com.easywash.util.EWConstants;
import com.google.common.base.Strings;

@Controller
@ResponseBody
//@RequestMapping(path = "/customer")
public class CustomerController {
	@Autowired
	private CustomerRepository userRepository;
	@Autowired
	private JMSTransactionController otherController;
	@Autowired
	private PackageRepository packageRepository;
	@Autowired private OrderhRepository orderRepository;
	
	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST, produces =
	 * "application/json") public String addCustomer(@RequestBody Customer student)
	 * {
	 * 
	 * student.setCreatets(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
	 * student.setActive("A"); student.setModifyusername("Admin");
	 * student.setModifyts(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
	 * 
	 * System.out.println("Testing...@@@."); userRepository.save(student); return
	 * "Success"; }
	 */

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = "application/json")
	public String addUser(@Valid @RequestBody Customer customer, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "Error";
		}

		// System.out.println("####"+String.valueOf(DateTimeUtil.createUniqueKey()));
		customer.setKey(String.valueOf(DateTimeUtil.createUniqueKey()));
		//customer.setCreatets(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
		customer.setActive("A");
		customer.setModifyusername("Admin");
		//customer.setModifyts(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
		userRepository.save(customer);
		model.addAttribute("customer", customer);

		// model.addAttribute("customer", userRepository.findAll());
		return "Success";
	}

	@GetMapping(path = "/customer/all")
	public @ResponseBody Iterable<Customer> getAllCustomers(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize) {
		// This returns a JSON or XML with the users
		// model.addAttribute("users", userRepository.findAll());
		// return "index";

		Pageable paging = PageRequest.of(pageNo, pageSize);
		return userRepository.findAll(paging);
	}

	@RequestMapping(value = "/customer/addc", method = RequestMethod.POST)
	public String add(@Valid @RequestBody Customer customer, BindingResult result, Model model) {
		model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
		return "manage";
	}

	@ResponseBody
	@PostMapping("/customer/addcust")
	public RedirectView addtr(@Valid @ModelAttribute("customer") Customer user, BindingResult result,
			RedirectAttributes atts, Model model) {
	
		/*
		 * @GetMapping("/addcust") public RedirectView addtr(@ModelAttribute("customer")
		 * Customer user, BindingResult result,RedirectAttributes atts,Model model) {
		 */
		if (result.hasErrors()) {

			
			  model.addAttribute(EWConstants.MODEL_CUSTOMER, user);
				/*
				 * List<String> listProfession = Arrays.asList("S", "F", "G");
				 * model.addAttribute("listProfession", listProfession);
				 */
			  List<String> listProfessions = new ArrayList<String>(); 
			  for (Easywashpackage t : packageRepository.findAll()) 
		        {
		        	listProfessions.add(t.getPname()); 
		        }
		  
		        // Return the converted collection 
		       
				model.addAttribute("listProfessions",listProfessions);
			  
			  
			  Pageable paging = PageRequest.of(0, 5);
			  model.addAttribute(EWConstants.MODEL_CUSTOMERS, user);
			 
			
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute("addCustMesssage",each.getDefaultMessage());
			}
			return new RedirectView("/home/1");
		}
		List<Easywashpackage> pack1=packageRepository.findByPname(user.getSubscribedpackage());
		if(pack1.size()<=0)
		{
			atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,"Not Valid Package Selected" );
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			
			return new RedirectView("/home/1");
		
		}
		List<Customer>ll=userRepository.findByMobile(user.getMobile());
		if(ll.size()>0)
		{
			Customer cust=ll.get(ll.size()-1);
			List<Orderh> listOrder=orderRepository.findBycref(String.valueOf(cust.getIdno()));
			List<Easywashpackage> pack=packageRepository.findByPname(cust.getSubscribedpackage());
			float packWeight=Float.parseFloat(pack.get(0).getLoadkg());
			float consumedWeight=0;
			for(Orderh eachOrder:listOrder)
			{
				consumedWeight+=Float.parseFloat(eachOrder.getWeightloadpiece());
			}
			//float leftWeight=;
			/*
			 * userRepository.findById(ll.get(0).getIdno()).orElseThrow(() -> new
			 * IllegalArgumentException("CId:" + ll.get(0).getIdno()+" already Exists."));
			 * atts.addFlashAttribute("alertClass", "alert-danger");
			 */
			user.setKey(String.valueOf(DateTimeUtil.createUniqueKey()));
			user.setActive("R");
			user.setModifyusername("Admin");
			if(!Strings.isNullOrEmpty(cust.getLname()))
			{
			user.setLname(String.valueOf(packWeight+Float.parseFloat(cust.getLname())-consumedWeight));
			atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,"Weight:" + (packWeight+Float.parseFloat(cust.getLname())-consumedWeight)+" Rolled Over to New CustomerID:"+user.getIdno());
			}
			else if(pack.get(0).getPname().equalsIgnoreCase(EWConstants.CUSTOMER_DEFAULT_PACKAGE))
			{
				user.setLname("");
				atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,"Weight: 0.00 Rolled Over to New CustomerID:"+user.getIdno());
				
			}
			else		
			{
			user.setLname(String.valueOf(packWeight-consumedWeight));
			atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,"Weight:" + String.valueOf(packWeight-consumedWeight)+" Rolled Over to New CustomerID:"+user.getIdno());
			
			}
			userRepository.save(user);
			
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			
			return new RedirectView("/home/1");
		}
		
		
		
		user.setKey(String.valueOf(DateTimeUtil.createUniqueKey()));
		user.setActive("A");
		user.setModifyusername("Admin");
		userRepository.save(user);
		Mail mail = new Mail();
		mail.setMailTo(user.getEmail());
		mail.setTemplate("SubscribePack");
		mail.setSubject(EWConstants.EMAIL_USER_SUBJECT);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("name", user.getFname());
		properties.put("location", user.getCreatets());
		properties.put("sign", user.getMobile());
		properties.put("orderNo", user.getIdno());
		properties.put("Amount", user.getSubscribedpackage());
		List<Object> list = new ArrayList<Object>();
		list.add("logo.png");
		mail.setAttachments(list);
		mail.setProps(properties);
		otherController.send(mail);
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG, "Sucessfully Added:"+user.getIdno()+". With name:"+user.getFname());
		return new RedirectView("/home/1");
	}

	@GetMapping("/customer/addcustomer")
	public RedirectView addcust() {
	
		return new RedirectView("/home");
	}

		@PostMapping("/customer/edit/{Idno}")
	public String showUpdateForm(@PathVariable("Idno") long id, @RequestBody Customer customer, BindingResult result,
			Model model) {

		if (result.hasErrors()) {

			return "edit";// change it to the name of the html page that you want to go
		} else {
			
			userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			userRepository.save(customer);

			return "manage";
		}
	}

	@PostMapping("/customer/editc")
	public RedirectView tt(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,RedirectAttributes atts, Model model) {
		Long id = customer.getIdno();
		if (result.hasErrors()) {
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				
				atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,each.getDefaultMessage());
			}
			return new RedirectView("/home/1");
		} else {

			Customer ccc = userRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			customer.setKey(ccc.getCkey());
			customer.setClead(ccc.getClead());
			customer.setCreatetts(ccc.getCreatetts());
			customer.setLname(ccc.getLname());
			customer.setModifyusername(ccc.getModifyusername());
			customer.setRef2(ccc.getRef2());
			customer.setRegion(ccc.getRegion());
			customer.setPincode(ccc.getPincode());

			userRepository.save(customer);
			
			
			
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG, "Sucessfully Updated:"+customer.getIdno()+". With name:"+customer.getFname());

			return new RedirectView("/home/1");
		}
	}

	@PostMapping("/customer/update/{Idno}")
	public String updateUser(@PathVariable("Idno") long id, @RequestBody Customer customer, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			customer.setIdno(id);
			return "update-user";
		}

		userRepository.save(customer);
		model.addAttribute(EWConstants.MODEL_CUSTOMERS, userRepository.findAll());
		return "index";
	}

	@GetMapping("/customer/delete/{Idno}")
	public RedirectView deleteUser(@PathVariable("Idno") long id, Model model, RedirectAttributes atts) {
		Customer user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userRepository.delete(user);
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG, "Sucessfully Deleted ID :"+id);

		return new RedirectView("/home/1");
	}

	@GetMapping("/customer/deleteC/{Idno}")
	public RedirectView deleteUsers(@PathVariable("Idno") long id, Model model, RedirectAttributes atts) {
		userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			return new RedirectView("/customer/delete/" + id);
	}

	@GetMapping("/customer/get/{mobile}")
	public ModelAndView showSingleUser(@PathVariable("mobile") Long mobile, Model model) {
		Customer user = new Customer();
		model.addAttribute(EWConstants.MODEL_CUSTOMER, user);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
		modelAndView.addObject(EWConstants.MODEL_ORDERS, new ArrayList<Orderh>());
		modelAndView.addObject(EWConstants.MODEL_ORDER, new Orderh());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		
	List<Long> list = new ArrayList<Long>();
		list.add(mobile);
		if(String.valueOf(mobile).length()==10)
		{
			PageRequest pageable = PageRequest.of(0, 500, Sort.by("ckey").descending());
			modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, userRepository.findByMobile(String.valueOf(mobile),pageable));
		modelAndView.addObject(EWConstants.MODEL_SEARCH_RECORD_COUNT, String.valueOf(userRepository.findByMobile(String.valueOf(mobile)).size()));
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		}
		else
		{
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS,userRepository.findAllById(list));	
		modelAndView.addObject(EWConstants.MODEL_SEARCH_RECORD_COUNT, "1");
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		}
		
		
	
		modelAndView.setViewName("manage");
		
		
		
		  Easywashpackage pack = new Easywashpackage(); 
		  modelAndView.addObject(EWConstants.MODEL_PACK, pack);
		  List<String> listProfession = new ArrayList<String>();
		  for (Easywashpackage t  : packageRepository.findAll())
		  { 
			  listProfession.add(t.getPname()); 
			  }
			  modelAndView.addObject(EWConstants.MODEL_LIST_PROFESSION,listProfession);
			 
		 
		return modelAndView;
		

	}

	@GetMapping("/customer/get")
	public RedirectView showConsolePage(Model model, RedirectAttributes atts) {

	        
		return new RedirectView("/home/1");
	}

	@GetMapping("/customer/getCMob/{mob}")
	public List<Customer> getLogin(@PathVariable("mob") String mob,Model model,RedirectAttributes atts) {
		return userRepository.findByMobile(mob);
	}
	@PostMapping("/customer/renew")
	public RedirectView renewCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,RedirectAttributes atts, Model model) {
		Long id = customer.getIdno();
		if (result.hasErrors()) {
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,each.getDefaultMessage());
			}
			return new RedirectView("/home/1");
		} else {
			Customer ccc = userRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			Customer renewC=new Customer();
			renewC.setKey(String.valueOf(DateTimeUtil.createUniqueKey()));
			renewC.setFname(customer.getFname());
			renewC.setLname(ccc.getLname());
			renewC.setSubscribedpackage(customer.getSubscribedpackage());
			renewC.setAddress(customer.getAddress());
			renewC.setPincode(ccc.getPincode());
			renewC.setMobile(customer.getMobile());
			renewC.setEmail(customer.getEmail());
			renewC.setActive("R");
			renewC.setModifyusername("Admin");
			renewC.setRegion(ccc.getRegion());
			renewC.setClead(String.valueOf(ccc.getIdno()));
			
			
		
				
				List<Orderh> listOrder=orderRepository.findBycref(String.valueOf(ccc.getIdno()));
				List<Easywashpackage> pack=packageRepository.findByPname(ccc.getSubscribedpackage());
				float packWeight=Float.parseFloat(pack.get(0).getLoadkg());
				float consumedWeight=0;
				for(Orderh eachOrder:listOrder)
				{
					consumedWeight+=Float.parseFloat(eachOrder.getWeightloadpiece());
				}
					if(!Strings.isNullOrEmpty(ccc.getLname()))
				{
					
					renewC.setLname(String.valueOf(packWeight+Float.parseFloat(ccc.getLname())-consumedWeight));
					atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG, "Sucessfully Renewed Package For Old CID:"+ccc.getIdno()+".With New CID:"+renewC.getIdno()+" and Package:"+renewC.getSubscribedpackage()+" with Carry forward Load of:"+ String.valueOf(packWeight+Float.parseFloat(ccc.getLname())-consumedWeight));

				}
				else	if(pack.get(0).getPname().equalsIgnoreCase(EWConstants.CUSTOMER_DEFAULT_PACKAGE))
				{
					renewC.setLname("");
					atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG, "Sucessfully Renewed Package For Old CID:"+ccc.getIdno()+".With New CID:"+renewC.getIdno()+" and Package:"+renewC.getSubscribedpackage()+" with Carry forward Load of:0.00");

				}
				else
				{
				
				renewC.setLname(String.valueOf(packWeight-consumedWeight));
				atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG, "Sucessfully Renewed Package For Old CID:"+ccc.getIdno()+".With New CID:"+renewC.getIdno()+" and Package:"+renewC.getSubscribedpackage()+" with Carry forward Load of:"+ String.valueOf(packWeight-consumedWeight));

				}
				
				
				userRepository.save(renewC);
			
			
			
			
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		
			return new RedirectView("/home/1");
		}
	}
	


}
