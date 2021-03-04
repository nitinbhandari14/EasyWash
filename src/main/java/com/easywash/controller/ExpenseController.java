package com.easywash.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.easywash.dao.OrderlinechargesRepository;
import com.easywash.dao.PackageRepository;
import com.easywash.model.Customer;
import com.easywash.model.Easywashpackage;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.EWConstants;

@Controller
@ResponseBody
public class ExpenseController {
	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private OrderlinechargesRepository expenseRepository;
	@Autowired
	private JMSTransactionController otherController;

	@ResponseBody
	@PostMapping("/expense/add")
	public RedirectView addtr(@Valid @ModelAttribute("expense") Orderlinecharges expense, BindingResult result,
			RedirectAttributes atts, Model model) {

		if (result.hasErrors()) {
			model.addAttribute(EWConstants.MODEL_EXPENSE, expense);
				List<ObjectError> errorList = result.getAllErrors();
			for (ObjectError each : errorList) {
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute(EWConstants.MODEL_SEARCH_EXPENSE_COUNT, each.getDefaultMessage());
			}
		return new RedirectView("/expense/1");
			}
			expense.setChargename(EWConstants.MODEL_EXPENSE);
		expenseRepository.save(expense);
		
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_EXPENSE_COUNT,
				"Sucessfully Added:" + expense.getIdno());
		return new RedirectView("/expense/1");
	}

	@PostMapping("/expense/edit/{Idno}")
	public String showUpdateForm(@PathVariable("Idno") long id, @RequestBody Easywashpackage pack, BindingResult result,
			Model model) {

		if (result.hasErrors()) {

			return "edit";// change it to the name of the html page that you want to go
		} else {
			packageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			packageRepository.save(pack);

			return "manage";
		}
	}


	@PostMapping("/expense/edite")
	public RedirectView tt(@Valid @ModelAttribute("expense") Orderlinecharges pack, BindingResult result,RedirectAttributes atts, Model model) {
		Long id = pack.getIdno();
		if (result.hasErrors()) {
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				
				atts.addFlashAttribute(EWConstants.MODEL_SEARCH_EXPENSE_COUNT,each.getDefaultMessage());
			}
			return new RedirectView("/expense/1");
		} else {

			Orderlinecharges ccc = expenseRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid Expense Id:" + id));

			ccc.setReserve(pack.getReserve());
			ccc.setReserveid(pack.getReserveid());
			
			ccc.setModifyusername("Moddify");

			expenseRepository.save(ccc);
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			atts.addFlashAttribute(EWConstants.MODEL_SEARCH_EXPENSE_COUNT, "Sucessfully Updated:"+ccc.getIdno());

			return new RedirectView("/expense/1");
		}
	}

	@GetMapping("/expense/delete/{Idno}")
	public RedirectView deleteUser(@PathVariable("Idno") long id, Model model, RedirectAttributes atts) {
		Orderlinecharges user = expenseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Expense Id:" + id));
		expenseRepository.delete(user);
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_EXPENSE_COUNT, "Sucessfully Deleted ID :"+id);

		return new RedirectView("/expense/1");
	}

	

	@GetMapping("/expense/get/{Idno}")
	public ModelAndView showSingleUser(@PathVariable("Idno") String expenseName, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, expenseRepository.findByreserve(expenseName));
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		  List<String> listProfession = new ArrayList<String>();
		  for (Easywashpackage t  : packageRepository.findAll())
		  { 
			  listProfession.add(t.getPname()); 
		  }
			modelAndView.addObject(EWConstants.MODEL_LIST_PROFESSION, listProfession);
		modelAndView.addObject(EWConstants.MODEL_SEARCH_EXPENSE_COUNT,expenseRepository.findByreserve(expenseName).size() +" :Records Fetched");
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		return modelAndView;
	}
	@GetMapping("/expense/getname/{pname}")
	public ModelAndView showMultiUser(@PathVariable("pname") String pname, Model model) {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		modelAndView.addObject(EWConstants.MODEL_PACKS, packageRepository.findByPnameContainingIgnoreCase(pname));
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		 List<String> listProfession = new ArrayList<String>();
		  for (Easywashpackage t  : packageRepository.findAll())
		  { 
			  listProfession.add(t.getPname()); 
		  }
			modelAndView.addObject(EWConstants.MODEL_LIST_PROFESSION, listProfession);
			modelAndView.addObject(EWConstants.MODEL_SEARCH_PACK_COUNT, packageRepository.findByPnameContainingIgnoreCase(pname).size());
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		
		
		return modelAndView;
	}

	@GetMapping("/expense/get")
	public RedirectView showConsolePage(Model model, RedirectAttributes atts) {

		return new RedirectView("/expense/1");
	}
	@GetMapping("/expense/getname")
	public RedirectView showConsolePagePack(Model model, RedirectAttributes atts) {

		return new RedirectView("/expense/1");
	}
		



}
