package com.easywash.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.PackageRepository;
import com.easywash.model.Customer;
import com.easywash.model.Easywashpackage;
import com.easywash.model.Orderh;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.EWConstants;

@Controller
@ResponseBody
public class InvoiceController {
	@Autowired
	private CustomerRepository userRepository;
	@Autowired
	private JMSTransactionController otherController;
	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private OrderhRepository orderRepository;

	@GetMapping("/invoice/get/{status}")
	public ModelAndView showSingleUser(@PathVariable("status") String status, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		/*
		 * List<String> list = new ArrayList<String>(); list.add(status);
		 */
		PageRequest pageable = PageRequest.of(0, 500, Sort.by("modifytts").descending());
		modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		modelAndView.addObject(EWConstants.MODEL_INVOICES, orderRepository.findByoref(status, pageable));
		modelAndView.addObject(EWConstants.MODEL_INVOICE, new Orderh());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		/*
		 * List<String> listProfession = Arrays.asList("S", "F", "G");
		 * modelAndView.addObject("listProfession", listProfession);
		 */
		// List<String> listOrderStatus = Arrays.asList("WIP", "Completed",
		// "Delivered");
		modelAndView.addObject(EWConstants.MODEL_ORDER_STATUSES, UtilController.getPostCompletedOrderStatus());

		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		return modelAndView;

	}

	@GetMapping("/invoice/get")
	public RedirectView showConsolePage(Model model, RedirectAttributes atts) {

		return new RedirectView("/invoice/1");
	}
	
	  @GetMapping("/invoice/createInvoice/{oid}") 
	  public ModelAndView  CreateInvoice(@PathVariable("oid") Long oid, Model model) { 
		  ModelAndView	  modelAndView = new ModelAndView();
	  
	
	  
	  Optional<Orderh> eachOrder=orderRepository.findById(oid);
	  Optional<Customer>  eachCustomer=userRepository.findById(Long.parseLong(eachOrder.get().getCref())); 
	  List<Easywashpackage>	  eachPackage=packageRepository.findByPname(eachCustomer.get().
	  getSubscribedpackage()); 
	  modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
	  modelAndView.addObject(EWConstants.MODEL_INVOICES,eachOrder.get());
	  modelAndView.addObject(EWConstants.MODEL_INVOICE, eachOrder.get());
	  modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, eachCustomer.get());
	  modelAndView.addObject(EWConstants.MODEL_CUSTOMER, eachCustomer.get());
	  modelAndView.addObject(EWConstants.MODEL_PACKS, eachPackage.get(0));
	  modelAndView.addObject(EWConstants.MODEL_PACK, eachPackage.get(0));
	  modelAndView.addObject(EWConstants.MODEL_ORDERS,new ArrayList<Orderh>());
	  modelAndView.addObject(EWConstants.MODEL_ORDER, new Orderh());
	  
	  modelAndView.addObject(EWConstants.MODEL_ORDER_STATUSES,
	  UtilController.getPostCompletedOrderStatus());
	  
	  modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
	  //modelAndView.setViewName("manage"); return modelAndView;
	  return modelAndView;
	  
	  }
	
		@PostMapping("/invoice/editi")
		// @RequestMapping(value = "/editc", method = RequestMethod.POST, produces =
		// "application/json")
		public RedirectView orderEdit(@Valid @ModelAttribute("order") Orderh order, BindingResult result,RedirectAttributes atts, Model model) {
			Long id = order.getIdno();
			if (result.hasErrors()) {
				List<ObjectError> errorList=result.getAllErrors();
				for(ObjectError each:errorList)
				{
					atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
					atts.addFlashAttribute(EWConstants.MODEL_MESSAGE_INVOICE,each.getDefaultMessage());
				}
				return new RedirectView("/invoice/1");
			}
			
			else
			{

			 Optional<Orderh> ccc=orderRepository.findById(id);
				if(ccc.get().getOref().equalsIgnoreCase(EWConstants.ORDER_STATUS_INVOICED))
				{
					ccc.get().setOref(order.getOref());
			    orderRepository.save(ccc.get());
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
				atts.addFlashAttribute(EWConstants.MODEL_MESSAGE_INVOICE, "Sucessfully Updated OrderID:"+order.getIdno()+".Status to:"+order.getOref());
				}
				else
				{
					atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
					atts.addFlashAttribute(EWConstants.MODEL_MESSAGE_INVOICE, "Order Status can't be updated to Previous Status.");
				}

				return new RedirectView("/invoice/1");
			}
		}

}
