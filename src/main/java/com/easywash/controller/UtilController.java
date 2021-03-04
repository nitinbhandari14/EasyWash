package com.easywash.controller;

import java.io.StringReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.easywash.dao.CustomerRepository;
import com.easywash.dao.OrderhRepository;
import com.easywash.dao.OrderlRepository;
import com.easywash.dao.OrderlinechargesRepository;
import com.easywash.dao.PackageRepository;
import com.easywash.dao.RecaptchaService;
import com.easywash.model.Customer;
import com.easywash.model.Easywashpackage;
import com.easywash.model.GenericWrap;
import com.easywash.model.Mail;
import com.easywash.model.OrderStatus;
import com.easywash.model.Orderh;
import com.easywash.model.Orderl;
import com.easywash.model.Orderlinecharges;
import com.easywash.model.User;
import com.easywash.util.DateTimeUtil;
import com.easywash.util.EWConstants;
import com.google.common.base.Strings;

@Controller
public class UtilController {
	// private static final Logger LOGGER =
	// LogManager.getLogger(UtilController.class);

	@Value("${google.recaptcha.secret}")
	String recaptchaSecret;
	@Value("${google.recaptcha.secret.status}")
	String recaptchaSecretStatus;
	@Autowired
	private CustomerRepository userRepository;
	// @Autowired UserRepository userRepository;
	@Autowired
	private OrderhRepository orderRepository;
	@Autowired
	private OrderlRepository orderlRepository;
	@Autowired
	private OrderlinechargesRepository expenseRepository;
	@Autowired
	RecaptchaService captchaService;
	@Autowired
	CustomerController customerService;
	@Autowired
	private JMSTransactionController otherController;
	@Autowired
	private PackageRepository packageRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

	@GetMapping("/login")
    public String loginGet(Model model, String error, String logout) {
		 if (securityService.isAuthenticated()) {
	            return "redirect:/home/1";
	        }

		
		 if (error != null)
		 
	            model.addAttribute("error", "Your username and password is invalid.");

	        if (logout != null)
	       
	            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }
	
	/*
	 * @PostMapping("/login") public String loginPost(User user, String error,String
	 * logout) { if (securityService.isAuthenticated()) {
	 * 
	 * //model.addAttribute(EWConstants.MODEL_USER, new OrderStatus()); //
	 * model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
	 * 
	 * return "index"; } return "login"; }
	 */
	 
	
	@GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
    		return "registration";
    }
	 
	 @PostMapping("/registration")
	    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult) {
		 
		// userValidator.validate(user, bindingResult);
		 
		 if (bindingResult.hasErrors()) {
			    return "manage";
	        }
		  userService.save(user);
		 // securityService.autoLogin(user.getUsername(), user.getPassword());
		  
		
		  return "login";
    }
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute(EWConstants.MODEL_USER, new OrderStatus());
		model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());

		return "index";
	}

	/*
	 * @GetMapping("/re") public String
	 * reindex(@ModelAttribute(EWConstants.MODEL_CUSTOMER) Customer
	 * customer, @ModelAttribute("user") OrderStatus user, Model model) {
	 * model.addAttribute(EWConstants.MODEL_USER, user);
	 * model.addAttribute(EWConstants.MODEL_CUSTOMER, customer);
	 * 
	 * return "redirect:/#contact-section"; }
	 */

	@ResponseBody
	@PostMapping("/Ostatus")
	public RedirectView signup(@Valid @ModelAttribute(EWConstants.MODEL_USER) OrderStatus user, BindingResult result,
			@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request,
			Model model, RedirectAttributes atts) {

		if (result.hasErrors()) {

			model.addAttribute(EWConstants.MODEL_USER, user);
			model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());

			List<ObjectError> errorList = result.getAllErrors();
			for (ObjectError each : errorList) {
				atts.addFlashAttribute("inputPar", each.getDefaultMessage());
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			}
			// return "redirect:/#order-status";
			return new RedirectView("/#order-status");
		}

		String ip = request.getRemoteAddr();

		String captchaVerifyMessage = captchaService.verifyRecaptcha(ip, recaptchaResponse, recaptchaSecretStatus);

		if (!captchaVerifyMessage.equals(new String("true"))) {

			atts.addFlashAttribute("messageStat", "Request Failed");
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			// return "redirect:/validerror";
			// return "redirect:/#order-status";
			return new RedirectView("/#order-status");
		}

		List<Long> list = new ArrayList<Long>();
		list.add(Long.getLong(user.getInputPar()));

		List<Orderh> oList = orderRepository.findBycref(user.getInputPar());

		if (oList.size() <= 0) {
			atts.addFlashAttribute("messageStat", "Invalid CustomerID:" + user.getInputPar());
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
		} else {
			atts.addFlashAttribute("messageStat", "OrderStatus:" + oList.get(oList.size() - 1).getOref());
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			atts.addFlashAttribute("messageStat1", "OrderID:" + oList.get(oList.size() - 1).getIdno());
			//atts.addFlashAttribute("alertClass", "alert-success");
			atts.addFlashAttribute("messageStat2", "Address:" + oList.get(oList.size() - 1).getRef1());
			//atts.addFlashAttribute("alertClass", "alert-success");
		}

		return new RedirectView("/#order-status");
	}

	@ResponseBody
	@PostMapping("/lead")
	public RedirectView addlead(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,
			@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request,
			RedirectAttributes atts, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("user", new OrderStatus());
			model.addAttribute(EWConstants.MODEL_CUSTOMER, customer);
			List<ObjectError> errorList = result.getAllErrors();
			for (ObjectError each : errorList) {
				atts.addFlashAttribute("inputlead", each.getDefaultMessage());
				atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			}
			return new RedirectView("/#contact-section");
		}

		String ip = request.getRemoteAddr();
		String captchaVerifyMessage = captchaService.verifyRecaptcha(ip, recaptchaResponse, recaptchaSecret);

		if (!captchaVerifyMessage.equals(new String("true"))) {

			Map<String, Object> response = new HashMap<String, Object>();
			response.put("message", captchaVerifyMessage);
			atts.addFlashAttribute("message", "Request Failed due to captcha");
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_FAIL);
			return new RedirectView("/#contact-section");
		}

		List<Customer> ll = userRepository.findByMobile(customer.getMobile());
		if (ll.size() > 0) {
			atts.addFlashAttribute("message", "Hello :" + ll.get(0).getFname() + " PickUp Registered for Package:"
					+ ll.get(0).getSubscribedpackage() + ".");
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			customer.setIdno(ll.get(0).getIdno());
			customer.setKey(ll.get(0).getCkey());
			customer.setActive(ll.get(0).getActive());
			customer.setSubscribedpackage(ll.get(0).getSubscribedpackage());
			customer.setPincode(ll.get(0).getPincode());
			customer.setEmail(ll.get(0).getEmail());
			customer.setFname(ll.get(0).getFname());
			customer.setAddress(ll.get(0).getAddress());
			customer.setKey(ll.get(0).getCkey());
			customer.setCreatetts(ll.get(0).getCreatetts());
			customer.setModifyusername(ll.get(0).getFname());
			userRepository.save(customer);
			return new RedirectView("/#contact-section");
		} else {
			customer.setEmail(EWConstants.GMAIL_ARVIND);
			customer.setKey(String.valueOf(DateTimeUtil.createUniqueKey()));
			customer.setActive("L");
			customer.setModifyusername("Admin");
			customer.setPincode(EWConstants.CUSTOMER_DEFAULT_PINCODE);
			customer.setSubscribedpackage(EWConstants.CUSTOMER_DEFAULT_PACKAGE);

		}

		userRepository.save(customer);
		Mail mail = new Mail();
		mail.setBcc(EWConstants.GMAIL_ARVIND);
		mail.setFrom(EWConstants.EASYWASH_INFO);
		mail.setMailTo(EWConstants.EASYWASH_ARVIND);
		mail.setTemplate("emailLead");
		mail.setSubject(EWConstants.EMAIL_LEAD_SUBJECT);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("name", customer.getFname());
		properties.put("time", customer.getCreatets());
		properties.put("mob", customer.getMobile());
		properties.put("id", customer.getIdno());
		properties.put("region", customer.getRegion());
		properties.put("msg", customer.getRef2());
		List<Object> list = new ArrayList<Object>();
		list.add("logo.png");
		mail.setAttachments(list);
		mail.setProps(properties);
		atts.addFlashAttribute("message", "Request registered with CID:" + customer.getIdno());
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		return new RedirectView("/#contact-section");
	}

	@GetMapping("/customer/searchCustRange/{fromDate}/{toDate}")
	public String getUsers(@PathVariable("fromDate") String from, @PathVariable("toDate") String to, Model model) {
		String fromDate = from.replaceAll("[^\\da-z ]", "");
		String toDate = to.replaceAll("[^\\da-z ]", "");

		if (Strings.isNullOrEmpty(from) || Strings.isNullOrEmpty(to)) {
			fromDate = "20190701010100000000";
			toDate = "20200701010100000000";
		}
		Customer user = new Customer();
		model.addAttribute("customer", user);
		List<String> listProfessions = new ArrayList<String>();
		for (Easywashpackage t : packageRepository.findAll()) {
			listProfessions.add(t.getPname());
		}
		model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfessions);

		List<Customer> users = userRepository.findByckeyBetween(fromDate + "00000000", toDate + "00000000");
		PageRequest pageable = PageRequest.of(0, 5);
		int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > users.size() ? users.size() : (start + pageable.getPageSize());
		Page<Customer> articlePage = new PageImpl<Customer>(users.subList(start, end), pageable, users.size());
		model.addAttribute(EWConstants.MODEL_CUSTOMERS, articlePage.getContent());
		model.addAttribute("activeArticleList", true);
		int totalPages = articlePage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);

		}

		return "manage";
	}

//@ResponseBody
	@GetMapping("/customer/searchCRange/{fromDate}/{toDate}")
	public String getCustomerBydate(@PathVariable("fromDate") String from, @PathVariable("toDate") String to,
			Model model, RedirectAttributes atts) {
		String fromDate = from.replaceAll("[^\\da-z ]", "");
		String toDate = to.replaceAll("[^\\da-z ]", "");

		if (Strings.isNullOrEmpty(from) || Strings.isNullOrEmpty(to)) {
			fromDate = "20190701010100000000";
			toDate = "20200701010100000000";
		}
		List<Customer> users = userRepository.findByckeyBetween(fromDate + "00000000", toDate + "00000000");

		GenericWrap<Customer> wrap = new GenericWrap<>();
		wrap.setgList(users);

		atts.addFlashAttribute(EWConstants.MODEL_CUSTOMERS, wrap);
		atts.addFlashAttribute("custTimeSearch", "s");
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_RECORD_COUNT, String.valueOf(users.size()));
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		return "redirect:/home/1";

	}

	@GetMapping(value = "/home/{page}")
	public String genericlistArticlesPageByPage(@PathVariable("page") int page,
			@ModelAttribute("customers") GenericWrap customers, @ModelAttribute("custTimeSearch") String flag,
			Model model, RedirectAttributes atts) {
		model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
		model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
	

		//Customer user = ;
		model.addAttribute("customer", new Customer());
		List<String> listProfession = new ArrayList<String>();
		for (Easywashpackage t : packageRepository.findAll()) {
			listProfession.add(t.getPname());
		}
		model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);
		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("modifytts").descending());

		Page<Customer> articlePage = userRepository.findAll(pageable);

		if ("s".equals(flag)) {

			PageRequest pageable1 = PageRequest.of(page - 1, 500, Sort.by("modifytts").descending());
			int start = (int) pageable1.getOffset();
			int end = (start + pageable1.getPageSize()) > customers.getgList().size() ? customers.getgList().size()
					: (start + pageable1.getPageSize());
			articlePage = new PageImpl<Customer>(customers.getgList().subList(start, end), pageable1,
					customers.getgList().size());
			atts.addFlashAttribute("custTimeSearch", "s");
			atts.addFlashAttribute(EWConstants.MODEL_SEARCH_RECORD_COUNT, String.valueOf(customers.getgList().size()));
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);

		}
		int totalPages = articlePage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute(EWConstants.MODEL_PAGE_NO, pageNumbers);
		}
		model.addAttribute("activeArticleList", true);
		model.addAttribute(EWConstants.MODEL_CUSTOMERS, articlePage.getContent());
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		Easywashpackage pack = new Easywashpackage();
		model.addAttribute(EWConstants.MODEL_PACK, pack);
		return "manage";

	}

	@GetMapping(value = "/pack/{page}")
	public String genericPackageArticlesPageByPage(@PathVariable("page") int page,
			@ModelAttribute("custTimeSearch") String flag, Model model, RedirectAttributes atts) {
		model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
		model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
		
		//Customer cust = ;
		model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
		//List<Customer> listCust = ;
		model.addAttribute(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		//Easywashpackage pack = ;
		model.addAttribute(EWConstants.MODEL_PACK, new Easywashpackage());

		List<String> listProfession = new ArrayList<String>();
		for (Easywashpackage t : packageRepository.findAll()) {
			listProfession.add(t.getPname());
		}
		model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);

		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("updatedtest").descending());

		Page<Easywashpackage> articlePage = packageRepository.findAll(pageable);

		int totalPages = articlePage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> packpageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("packpageNumbers", packpageNumbers);
		}
		model.addAttribute("activeArticleList", true);
		model.addAttribute(EWConstants.MODEL_PACKS, articlePage.getContent());

		atts.addFlashAttribute("searchPackCount", articlePage.getContent().size());
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);

		return "manage";

	}

	@GetMapping(value = "/order/{page}")
	public String genericOrderPageByPage(@PathVariable("page") int page, @ModelAttribute("custTimeSearch") String flag,
			Model model, RedirectAttributes atts) {
		model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
		model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
		
		model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
		model.addAttribute(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		model.addAttribute(EWConstants.MODEL_PACK, new Easywashpackage());
		model.addAttribute(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		model.addAttribute(EWConstants.MODEL_ORDER, new Orderh());
		List<String> listProfession = new ArrayList<String>();
		for (Easywashpackage t : packageRepository.findAll()) {
			listProfession.add(t.getPname());
		}
		model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);
		model.addAttribute(EWConstants.MODEL_ORDER_STATUSES, UtilController.getOrderStatus());
		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("modifytts").descending());
		Page<Orderh> articlePage = orderRepository.findAll(pageable);
		int totalPages = articlePage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> packpageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute(EWConstants.MODEL_ORDER_PAGE_NO, packpageNumbers);
		}
		model.addAttribute("activeArticleList", true);
		model.addAttribute(EWConstants.MODEL_ORDERS, articlePage.getContent());
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_ORDER_COUNT, articlePage.getContent().size() + " Record Found");
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		return "manage";
	}

	@GetMapping(value = "/invoice/{page}")
	public String genericInvoicePageByPage(@PathVariable("page") int page,
			@ModelAttribute("custTimeSearch") String flag, Model model, RedirectAttributes atts) {
		model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
		model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
		model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
		
		model.addAttribute(EWConstants.MODEL_ORDER, new Orderh());
		model.addAttribute(EWConstants.MODEL_ORDERS, new ArrayList<Orderh>());
		model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
		model.addAttribute(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
		model.addAttribute(EWConstants.MODEL_PACK, new Easywashpackage());
		model.addAttribute(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
		model.addAttribute(EWConstants.MODEL_INVOICE, new Orderh());
		List<String> listProfession = new ArrayList<String>();
		for (Easywashpackage t : packageRepository.findAll()) {
			listProfession.add(t.getPname());
		}
		model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);
		model.addAttribute(EWConstants.MODEL_ORDER_STATUSES, UtilController.getPostCompletedOrderStatus());
		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("modifytts").descending());
		// Page<Orderh> articlePage =orderRepository.findAll(pageable);

		Page<Orderh> articlePage = orderRepository.findByoref(EWConstants.ORDER_STATUS_COMPLETED, pageable);
		int totalPages = articlePage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> packpageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute(EWConstants.MODEL_INVOICE_PAGE_NO, packpageNumbers);
		}
		model.addAttribute("activeArticleList", true);
		model.addAttribute(EWConstants.MODEL_INVOICES, articlePage.getContent());
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_INVOICE_COUNT, articlePage.getContent().size() + " Record Found");
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
		return "manage";
	}

	public void sendEmail() {

	}

	@GetMapping("/userPage")
	public String welcome(Principal principal, Model model, RedirectAttributes atts) {

		atts.addFlashAttribute("name", principal.getName());
		return "welcome";
	}

	@GetMapping("/order/searchORange/{fromDate}/{toDate}")
	public String getOrdersBydate(@PathVariable("fromDate") String from, @PathVariable("toDate") String to, Model model,
			RedirectAttributes atts) {
		String fromDate = from.replaceAll("[^\\da-z ]", "");
		String toDate = to.replaceAll("[^\\da-z ]", "");

		if (Strings.isNullOrEmpty(from) || Strings.isNullOrEmpty(to)) {
			fromDate = "20190701010100000000";
			toDate = "20200701010100000000";
		}
		List<Orderh> orders = orderRepository.findByokeyBetween(fromDate + "00000000", toDate + "00000000");
		GenericWrap<Orderh> wrap = new GenericWrap<>();
		wrap.setgList(orders);

		atts.addFlashAttribute(EWConstants.MODEL_ORDERS, wrap);
		atts.addFlashAttribute(EWConstants.MODEL_CUST_TIME_SEARCH, "s");
		atts.addFlashAttribute(EWConstants.MODEL_SEARCH_RECORD_COUNT, String.valueOf(orders.size()));
		atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);

		return "redirect:/order/1";

	}

	public static List<String> getOrderStatus() {
		return Arrays.asList(EWConstants.ORDER_STATUS_WIP, EWConstants.ORDER_STATUS_COMPLETED);
	}

	public static List<String> getPostCompletedOrderStatus() {
		return Arrays.asList(EWConstants.ORDER_STATUS_INVOICED, EWConstants.ORDER_STATUS_DELIVERED);
	}
	
	public static String json2XML(String str)
	{
		JSONObject jsonObject = new JSONObject(str);
		return XML.toString(jsonObject);
	}
	 public static Document convertStringToXMLDocument(String xmlString) 
	    {
	        //Parser that produces DOM object trees from XML content
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         
	        //API to obtain DOM Document instance
	        DocumentBuilder builder = null;
	        try
	        {
	            //Create DocumentBuilder with default configuration
	            builder = factory.newDocumentBuilder();
	             
	            //Parse the content to Document object
	            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
	            return doc;
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 @GetMapping(value = "/expense/{page}")
		public String genericExpensePageByPage(@PathVariable("page") int page,
				@ModelAttribute("custTimeSearch") String flag, Model model, RedirectAttributes atts) {
		 	model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		 	model.addAttribute(EWConstants.MODEL_INVOICES, new ArrayList<Orderh>());
			//model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
			model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
			model.addAttribute(EWConstants.MODEL_PAYMENTS, new ArrayList<Orderl>());
			model.addAttribute(EWConstants.MODEL_ORDER, new Orderh());
			model.addAttribute(EWConstants.MODEL_ORDERS, new ArrayList<Orderh>());
			model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
			model.addAttribute(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
			model.addAttribute(EWConstants.MODEL_PACK, new Easywashpackage());
			model.addAttribute(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
			model.addAttribute(EWConstants.MODEL_INVOICE, new Orderh());
			List<String> listProfession = new ArrayList<String>();
			for (Easywashpackage t : packageRepository.findAll()) {
				listProfession.add(t.getPname());
			}
			model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);
			model.addAttribute(EWConstants.MODEL_ORDER_STATUSES, UtilController.getPostCompletedOrderStatus());
			PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("modifytts").descending());
		
			Page<Orderlinecharges> articlePage = expenseRepository.findBychargename(EWConstants.MODEL_EXPENSE, pageable);
			int totalPages = articlePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> packpageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute(EWConstants.MODEL_EXPENSE_PAGE_NO, packpageNumbers);
			}
			model.addAttribute("activeArticleList", true);
			model.addAttribute(EWConstants.MODEL_EXPENSES, articlePage.getContent());
			atts.addFlashAttribute(EWConstants.MODEL_SEARCH_EXPENSE_COUNT, articlePage.getContent().size() + " Record Found");
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			return "manage";
		}
	 @GetMapping(value = "/orderline/{page}")
		public String genericPaymentPageByPage(@PathVariable("page") int page,
				@ModelAttribute("custTimeSearch") String flag, Model model, RedirectAttributes atts) {
		 	model.addAttribute(EWConstants.MODEL_EXPENSE, new Orderlinecharges());
		 	model.addAttribute(EWConstants.MODEL_INVOICES, new ArrayList<Orderh>());
			model.addAttribute(EWConstants.MODEL_PAYMENT, new Orderl());
			model.addAttribute(EWConstants.MODEL_EXPENSES, new ArrayList<Orderlinecharges>());
			model.addAttribute(EWConstants.MODEL_ORDER, new Orderh());
			model.addAttribute(EWConstants.MODEL_ORDERS, new ArrayList<Orderh>());
			model.addAttribute(EWConstants.MODEL_CUSTOMER, new Customer());
			model.addAttribute(EWConstants.MODEL_CUSTOMERS, new ArrayList<Customer>());
			model.addAttribute(EWConstants.MODEL_PACK, new Easywashpackage());
			model.addAttribute(EWConstants.MODEL_PACKS, new ArrayList<Easywashpackage>());
			model.addAttribute(EWConstants.MODEL_INVOICE, new Orderh());
			List<String> listProfession = new ArrayList<String>();
			for (Easywashpackage t : packageRepository.findAll()) {
				listProfession.add(t.getPname());
			}
			model.addAttribute(EWConstants.MODEL_LIST_PROFESSION, listProfession);
			model.addAttribute(EWConstants.MODEL_ORDER_STATUSES, UtilController.getPostCompletedOrderStatus());
			PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by("modifytts").descending());
		
			Page<Orderl> articlePage = orderlRepository.findAll( pageable);
			int totalPages = articlePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> packpageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute(EWConstants.MODEL_PAYMENT_PAGE_NO, packpageNumbers);
			}
			model.addAttribute("activeArticleList", true);
			model.addAttribute(EWConstants.MODEL_PAYMENTS, articlePage.getContent());
			atts.addFlashAttribute(EWConstants.MODEL_SEARCH_PAYMENT_COUNT, articlePage.getContent().size() + " Record Found");
			atts.addFlashAttribute(EWConstants.MODEL_ALERT_CLASS, EWConstants.MODEL_ALERT_SUCCESS);
			return "manage";
	 }
/*		}
	 @RequestMapping("/login.html")
	  public String login() {
	    return "login.html";
	  }*/

	  // Login form with error
	  @RequestMapping("/login-error.html")
	  public String loginError(Model model) {
	    model.addAttribute("loginError", true);
	    return "login.html";
	  }
}
