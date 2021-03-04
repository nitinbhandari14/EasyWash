package com.easywash.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.easywash.dao.PackageRepository;
import com.easywash.model.Customer;
import com.easywash.model.Easywashpackage;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.util.EWConstants;

@Controller
@ResponseBody
public class PackageController {
	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private JMSTransactionController otherController;

	@ResponseBody
	@PostMapping("/package/addp")
	public RedirectView addtr(@Valid @ModelAttribute("pack") Easywashpackage pack, BindingResult result,
			RedirectAttributes atts, Model model) {

		if (result.hasErrors()) {
			model.addAttribute(EWConstants.MODEL_PACKAGE, pack);
			List<String> listProfession = Arrays.asList("S", "F", "G");
			model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);
			//Pageable paging = PageRequest.of(0, 5,Sort.by("ckey").descending());
			model.addAttribute(EWConstants.MODEL_PACKAGE, pack);
			List<ObjectError> errorList = result.getAllErrors();
			for (ObjectError each : errorList) {
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				atts.addFlashAttribute(EWConstants.MODEL_ADD_PACK_MESSAGE, each.getDefaultMessage());
			}
		return new RedirectView("/pack/1");
			}
		//pack.setCreatets(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
		//pack.setModifyts(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
		pack.setModifyuser("Admin");
		packageRepository.save(pack);
		
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_ADD_PACK_MESSAGE,
				"Sucessfully Added:" + pack.getIdno() + ". With name:" + pack.getPname());
		return new RedirectView("/pack/1");
		// return("/home");
	}

	// @ModelAttribute("customer") Customer user, BindingResult result
	@PostMapping("/package/edit/{Idno}")
	// @GetMapping(value = "/edit/{Idno}", method = RequestMethod.POST)
	public String showUpdateForm(@PathVariable("Idno") long id, @RequestBody Easywashpackage pack, BindingResult result,
			Model model) {

		if (result.hasErrors()) {

			return "edit";// change it to the name of the html page that you want to go
		} else {
			packageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			// System.out.println("ssd"+model.getAttribute("address"));
			// model.addAttribute("user", user);

			packageRepository.save(pack);

			return "manage";
		}
	}

	/*
	 * @PostMapping("/addcust") public String addtr(@ModelAttribute("customer")
	 * Customer user, BindingResult result)
	 */
	@PostMapping("/package/editc")
	// @RequestMapping(value = "/editc", method = RequestMethod.POST, produces =
	// "application/json")
	public RedirectView tt(@Valid @ModelAttribute("pack") Easywashpackage pack, BindingResult result,RedirectAttributes atts, Model model) {
		Long id = pack.getIdno();
		if (result.hasErrors()) {
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError each:errorList)
			{
				//System.out.println(each.+"@@@@@@@@@@@@@@@@@@@@@@@@@@@"+each.getDefaultMessage());
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
				
				atts.addFlashAttribute(EWConstants.MODEL_ADD_PACK_MESSAGE,each.getDefaultMessage());
			}
			return new RedirectView("/pack/1");
		} else {

			Easywashpackage ccc = packageRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

			ccc.setPname(pack.getPname());
			ccc.setPrice(pack.getPrice());
			ccc.setValiddays(pack.getValiddays());
			ccc.setLoadkg(pack.getLoadkg());
			ccc.setTotalpiece(pack.getTotalpiece());
			//ccc.setModifyts(DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ssZ"));
			ccc.setModifyuser("Moddify");

			packageRepository.save(ccc);
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			atts.addFlashAttribute(EWConstants.MODEL_ADD_PACK_MESSAGE, "Sucessfully Updated:"+ccc.getIdno()+". Name:"+ccc.getPname());

			return new RedirectView("/pack/1");
		}
	}

	@GetMapping("/package/delete/{Idno}")
	public RedirectView deleteUser(@PathVariable("Idno") long id, Model model, RedirectAttributes atts) {
		Easywashpackage user = packageRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		packageRepository.delete(user);
		// model.addAttribute("customers", userRepository.findAll());

		/*
		 * Customer userc = new Customer(); model.addAttribute("customer", userc);
		 * List<String> listProfession = Arrays.asList("S", "F", "G");
		 * model.addAttribute("listProfession", listProfession); Pageable paging =
		 * PageRequest.of(0,5); model.addAttribute("customers",
		 * userRepository.findAll(paging));
		 */
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		atts.addFlashAttribute(EWConstants.MODEL_ADD_PACK_MESSAGE, "Sucessfully Deleted ID :"+id);

		return new RedirectView("/pack/1");
	}

	/*
	 * @GetMapping("/package/deleteP/{pname}") public RedirectView
	 * deleteUsers(@PathVariable("pname") String pname, Model model,
	 * RedirectAttributes atts) { packageRepository.findByPname(pname); //
	 * userRepository.delete(user); // model.addAttribute("customers",
	 * userRepository.findAll());
	 * 
	 * 
	 * Customer userc = new Customer(); model.addAttribute("customer", userc);
	 * List<String> listProfession = Arrays.asList("S", "F", "G");
	 * model.addAttribute("listProfession", listProfession); Pageable paging =
	 * PageRequest.of(0,5); model.addAttribute("customers",
	 * userRepository.findAll(paging));
	 * 
	 * // ​redirectAttrs.addAttribute("Idno", id); //
	 * ​redirectAttrs.addFlashAttribute("message", "Account created!");
	 * 
	 * return new RedirectView("/package/delete/" + pname); }
	 */

	@GetMapping("/package/get/{Idno}")
	public ModelAndView showSingleUser(@PathVariable("Idno") Long Idno, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		List<Long> list = new ArrayList<Long>();
		list.add(Idno);
		modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		modelAndView.addObject(EWConstants.MODEL_PACKS, packageRepository.findAllById(list));
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		List<String> listProfession = Arrays.asList("S", "F", "G");
		modelAndView.addObject(EWConstants.MODEL_LIST_PROFESSION, listProfession);
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		return modelAndView;
	}
	@GetMapping("/package/getname/{pname}")
	public ModelAndView showMultiUser(@PathVariable("pname") String pname, Model model) {
		ModelAndView modelAndView = new ModelAndView();
		/*
		 * List<String> list = new ArrayList<String>(); list.add(pname);
		 */
		modelAndView.addObject(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		modelAndView.addObject(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		modelAndView.addObject(EWConstants.MODEL_PAYMENT, new Orderl());
		modelAndView.addObject(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	
		modelAndView.addObject(EWConstants.MODEL_PACKS, packageRepository.findByPnameContainingIgnoreCase(pname));
		modelAndView.addObject(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		modelAndView.addObject(EWConstants.MODEL_CUSTOMER, new Customer());
		modelAndView.addObject(EWConstants.MODEL_PACK, new Easywashpackage());
		List<String> listProfession = Arrays.asList("S", "F", "G");
		modelAndView.addObject(EWConstants.MODEL_LIST_PROFESSION, listProfession);
		modelAndView.addObject(EWConstants.MODEL_SEARCH_PACK_COUNT, packageRepository.findByPnameContainingIgnoreCase(pname).size());
		modelAndView.addObject(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		modelAndView.setViewName("manage");
		
		
		return modelAndView;
	}

	@GetMapping("/package/get")
	public RedirectView showConsolePage(Model model, RedirectAttributes atts) {

		return new RedirectView("/pack/1");
	}
	@GetMapping("/package/getname")
	public RedirectView showConsolePagePack(Model model, RedirectAttributes atts) {

		return new RedirectView("/pack/1");
	}
		

@RequestMapping(value = "/pautocomplete")
    @ResponseBody
    public List<String> autoName(){
        //List<String> designation = dao.getDesignation(term);
		
		List<String> cltn = new ArrayList<String>(); 
		  
       
        for (Easywashpackage t : packageRepository.findAll()) 
        {
            cltn.add(t.getPname()); 
        }
  
        // Return the converted collection 
        return cltn; 
		
       // return null;
    }

}
