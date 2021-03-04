package com.easywash.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.OrderlRepository;
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
public class OrderlController {
	@Autowired private CustomerRepository userRepository;
	@Autowired private PackageRepository packageRepository;
	@Autowired private OrderhRepository orderRepository;
	@Autowired private OrderlRepository orderLineRepository;
	
	@Autowired
	private JMSTransactionController otherController;
	@Autowired
	private UtilController utilController;
	
	@GetMapping("/orderline/get/{cid}")
	public ModelAndView showSingleUser(@PathVariable("cid") Long cid, Model model) throws IllegalArgumentException, Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		//modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
		modelAndView.addObject(EWConstants.MODEL_ORDERS, new ArrayList<Orderh>());
		modelAndView.addObject(EWConstants.MODEL_ORDER, new Orderh());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		
		
		List<Long> list = new ArrayList<Long>();
		list.add(cid);
		Orderl ccc = orderLineRepository.findByolkey(String.valueOf(cid));
		if(Strings.isNullOrEmpty(String.valueOf(ccc.getIdno())))
		{
			modelAndView.addObject(EWConstants.MODEL_SEARCH_PAYMENT_COUNT, "No InvoiceKey:"+cid+", present in the System.");		
			modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			modelAndView.setViewName("manage");
			return modelAndView;
		
		}
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, ccc);
		
		/*
		 * List<Easywashpackage>
		 * packCalc=packageRepository.findByPname(ccc.get().getSubscribedpackage());
		 * Easywashpackage pack =packCalc.get(0); pack.getLoadkg();pack.getTotalpiece();
		 * 
		 * modelAndView.addObject("daysLeft",DateTimeUtil.diffInDaysWithFormat(pack.
		 * getValiddays(),DateTimeUtil.timeDiffFromCreateTSWRTCreateORModf(ccc.get().
		 * getCreatetts())));//-Integer.parseInt(pack.getValiddays()));
		 * 
		 * //logic to calculate leftover weight List<Orderh>
		 * listOrder=orderRepository.findBycref(String.valueOf(cid)); float
		 * previousWeight=0; if(!Strings.isNullOrEmpty(ccc.get().getLname()))
		 * previousWeight=Float.parseFloat(ccc.get().getLname());
		 * 
		 * float packWeight=Float.parseFloat(pack.getLoadkg()); float consumedWeight=0;
		 * for(Orderh eachOrder:listOrder) {
		 * consumedWeight+=Float.parseFloat(eachOrder.getWeightloadpiece()); }
		 * 
		 * modelAndView.addObject("loadLeft",packWeight+previousWeight-consumedWeight);
		 */		
		//modelAndView.addObject("ocustomers",userRepository.findAllById(list));	
		//List<Orderh> listExistingOrder=orderRepository.findBycref(String.valueOf(cid));
		modelAndView.addObject(EWConstants.MODEL_SEARCH_PAYMENT_COUNT, "Found InvoiceKey");		
			modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
	//	modelAndView.addObject("pack", pack);
		return modelAndView;
		

	}

	/*
	 * @GetMapping("/orderline/get") public RedirectView
	 * showConsolePageOrderline(Model model, RedirectAttributes atts) { return new
	 * RedirectView("/orderline/1"); }
	 */
	@GetMapping("/orderline/get")
	public RedirectView showConsolePage(Model model, RedirectAttributes atts) {

	
        
		return new RedirectView("/orderline/1");
	}
	
	@ResponseBody
	@PostMapping("/orderline/addOrder")
	public RedirectView addtr(@Valid @ModelAttribute("customer") Customer user,@RequestParam("cLoad") String cLoad, @RequestParam("comments") String comments, BindingResult result,
			RedirectAttributes atts, Model model) {
		if (result.hasErrors()) {

			
			  model.addAttribute(EWConstants.MODEL_CUSTOMER, user);
				
			  List<String> listProfessions = new ArrayList<String>(); 
			  for (Easywashpackage t : packageRepository.findAll()) 
		        {
		        	listProfessions.add(t.getPname()); 
		        }
		  
				model.addAttribute(EWConstants.MODEL_LIST_PROFESSION,listProfessions);
			  
			  
			  Pageable paging = PageRequest.of(0, 5);
			  model.addAttribute(EWConstants.MODEL_CUSTOMERS, user);
			 
			
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute(EWConstants.MODEL_CUST_MSG,each.getDefaultMessage());
			}
			return new RedirectView("/orderline/1");
		}
		if(Strings.isNullOrEmpty(cLoad))
		{
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			atts.addFlashAttribute(EWConstants.MODEL_MESSAGE_ORDER,"Enter Current Load and Retry");
			return new RedirectView("/order/1");
		}
		
		  Customer ccc = userRepository.findById(user.getIdno()) .orElseThrow(() -> new
		  IllegalArgumentException("Invalid user Id:" + user.getIdno()));
		
		  Orderh order=new Orderh();
		  order.setOkey(String.valueOf(DateTimeUtil.createUniqueKey()));
		  order.setWeightloadpiece(cLoad);
		  order.setNotes(comments);
		  order.setCref(String.valueOf(ccc.getIdno()));
		  order.setRef1(ccc.getAddress());
		  order.setRef2(ccc.getRef2());
		  List<Easywashpackage> packCalc=packageRepository.findByPname(ccc.getSubscribedpackage());
			Easywashpackage pack =packCalc.get(0);
			order.setPref(pack.getPname());
			if((pack.getPname().equalsIgnoreCase("Lead")))
			order.setPayment(EWConstants.PAYMENT_POSTPAID);
			order.setPayment(EWConstants.PAYMENT_PREPAID);
			order.setOref(EWConstants.ORDER_STATUS_CREATED);
			
		  orderRepository.save(order);
	
		
		
		
	
		
		  Mail mail = new Mail(); 
		  mail.setMailTo(ccc.getEmail()); 
		  mail.setTemplate("Order");
		  mail.setSubject(EWConstants.EMAIL_ORDER_SUBJECT+order.getIdno());
		  Map<String, Object> properties = new
		  HashMap<String, Object>();
		  properties.put("name", ccc.getFname());
		  properties.put("location", order.getCreatetts());
		  properties.put("currentWeight",
		  order.getWeightloadpiece()); properties.put("orderNo", order.getIdno());
		  properties.put("sPack", ccc.getSubscribedpackage());
		  mail.setProps(properties); otherController.send(mail);
		 
			atts.addFlashAttribute(EWConstants.MODEL_MESSAGE_ORDER, "OrderID Created:" + order.getIdno());
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);

		return new RedirectView("/order/1");
	}
	@GetMapping("/orderline/get/{Idno}")
	public ModelAndView showSingleOrder(@PathVariable("Idno") Long Idno, Model model) {
		model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
		model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		ModelAndView modelAndView = new ModelAndView();
		List<Long> list = new ArrayList<Long>();
		list.add(Idno);
		modelAndView.addObject(EWConstants.MODEL_ORDERS, orderRepository.findAllById(list));
		modelAndView.addObject(EWConstants.MODEL_ORDER, new Orderh());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		modelAndView.addObject(EWConstants.MODEL_ORDER_STATUSES, UtilController.getOrderStatus());
		
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		return modelAndView;
	}
	@PostMapping("/orderline/edito")
		public RedirectView orderEdit(@Valid @ModelAttribute("order") Orderh order, BindingResult result,RedirectAttributes atts, Model model) {
		Long id = order.getIdno();
		if (result.hasErrors()) {
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute(EWConstants.MODEL_SEARCH_ORDER_COUNT,each.getDefaultMessage());
			}
			return new RedirectView("/orderline/1");
		}
		
		else
		{

		 Optional<Orderh> ccc=orderRepository.findById(id);//.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		    order.setIdno(ccc.get().getIdno());
		    order.setModifyusername("Admin");
		    order.setCref(ccc.get().getCref());
		    order.setCreatetts(ccc.get().getCreatetts());
		    order.setOkey(ccc.get().getOkey());
		    order.setPayment(ccc.get().getPayment());
		    order.setPref(ccc.get().getPref());
		    order.setRef1(ccc.get().getRef1());
		    order.setRef2(ccc.get().getRef2());
		    order.setWeightloadpiece(ccc.get().getWeightloadpiece());
		    order.setNotes(ccc.get().getNotes());
			
			
			if(ccc.get().getOref().equalsIgnoreCase(EWConstants.ORDER_STATUS_WIP)||ccc.get().getOref().equalsIgnoreCase(EWConstants.ORDER_STATUS_CREATED))
			{
		    orderRepository.save(order);
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			atts.addFlashAttribute(EWConstants.MODEL_SEARCH_ORDER_COUNT, "Sucessfully Updated OrderID:"+order.getIdno()+".Status to:"+order.getOref());
			}
			else
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute(EWConstants.MODEL_SEARCH_ORDER_COUNT, "Order Status can't be updated to Previous Status.");
			}

			return new RedirectView("/orderline/1");
		}
	}
	@GetMapping("/orderline/delete/{Idno}")
	public RedirectView deleteUser(@PathVariable("Idno") long id, Model model, RedirectAttributes atts) {
		Orderl order = orderLineRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Invoice Id:" + id));
		orderLineRepository.delete(order);
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_PAYMENT_COUNT, "Sucessfully Deleted InvoiceID :"+id);

		return new RedirectView("/orderline/1");
	}
	@GetMapping("/orderline/getCID/{cid}")
	public ModelAndView showMultiUser(@PathVariable("cid") String cid, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
		model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		
		modelAndView.addObject(EWConstants.MODEL_ORDERS, orderRepository.findBycref(cid));
		modelAndView.addObject(EWConstants.MODEL_ORDER, new Orderh());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		modelAndView.addObject(EWConstants.MODEL_ORDER_STATUSES, UtilController.getOrderStatus());
		modelAndView.addObject(EWConstants.MODEL_SEARCH_ORDER_COUNT, orderRepository.findBycref(cid).size()+" Record Fetched.");
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		
		
		return modelAndView;
	}
	@GetMapping("/orderline/getCID")
	public RedirectView showAllOrder(Model model, RedirectAttributes atts) {

	
        
		return new RedirectView("/orderline/1");
	}
	
}
