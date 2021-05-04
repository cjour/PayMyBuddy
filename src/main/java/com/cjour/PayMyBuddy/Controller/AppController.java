package com.cjour.PayMyBuddy.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cjour.PayMyBuddy.Entity.Transaction;
import com.cjour.PayMyBuddy.Entity.User;
import com.cjour.PayMyBuddy.Service.LoginService;
import com.cjour.PayMyBuddy.Service.TransactionServiceImpl;
import com.cjour.PayMyBuddy.Service.UserRepository;

@Controller
public class AppController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private TransactionServiceImpl transactionServiceImpl;

	@Autowired
	private UserRepository userService;

	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/register")
	public ModelAndView showRegisterForm(ModelAndView md, User user) {
		md.addObject("user", user);
		md.setViewName("register");
		return md;
	}

	@PostMapping("/register_action")
	public String register(User user) throws Exception {
		loginService.registerUser(user);
		if (userService.findByUserName(user.getUserName()) == null) {
			userService.save(user);
			return "register_success";
		} else {
			throw new Exception("This username is already used, please choose another one.");
		}
	}

	@GetMapping("/login")
	public ModelAndView login(ModelAndView md, User user) {
		md.addObject("user", user);
		md.setViewName("login");
		return md;
	}

	@PostMapping("/login_action")
	public String loginAction(HttpServletRequest req, User user) {
		loginService.authenticateUser(req, user);
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
			return "dashboardUser";
		}

		return "login";
	}

	@GetMapping("/transfer")
	public ModelAndView transferView(ModelAndView md) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			
			User user = userService.findByUserName(((UserDetails) principal).getUsername());
			List<Transaction> transactions = transactionServiceImpl.getAllTransactionsByUser(user);
	 		 
			md.addObject("setOfTransactions", transactions);
			md.addObject("user", user);
			md.setViewName("transfer");
			
			return md;
		}
		
		return null;
	}
	
	@GetMapping("/profile")
	public ModelAndView profileView(ModelAndView md) throws Exception{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			User user = userService.findByUserName(((UserDetails) principal).getUsername());
			md.addObject("user", user);
			md.setViewName("profile");
			return md;
		} else {
			throw new Exception("Error occured : it seems you are not logged in.");
		}
	}

	@GetMapping("/contact")
	public String contactView(Model md) {

		List<User> userThatCanBeAdded = new ArrayList<>();

		User principalUser = userService.findByUserName(
				((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		List<User> users = userService.findAll();
		Set<User> contactListOfPrincipalUser = principalUser.getContacts();
		if (!contactListOfPrincipalUser.isEmpty()) {
			for (User user : users) {
				for (User contact : contactListOfPrincipalUser) {
					if (!user.getUserName().equals(contact.getUserName())) {
						userThatCanBeAdded.add(user);
					}
				}
			}
		}

		md.addAttribute("users", userThatCanBeAdded);
		md.addAttribute("user", principalUser);
		return "contact";
	}

	@GetMapping("/logout")
	public String fetchSignoutSite(HttpServletRequest request) throws Exception {
		SecurityContextHolder.clearContext();
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			return "home";
		} else {
			throw new Exception("Error occured : it seems you are not logged out correctly.");
		}
	}

	@Transactional
	@PostMapping("/transfer")
	public String transferAction(@RequestParam(value = "beneficiary", required = true) String beneficiary,
								@RequestParam(value = "description", required = true) String description,
								@RequestParam(value = "amount", required = true) double amount, ModelAndView md) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			User user = userService.findByUserName(((UserDetails) principal).getUsername());
			User beneficiaryUser = userService.findByUserName(beneficiary);
			transactionServiceImpl.transfer(user, beneficiaryUser, description, amount);
			md.addObject("user", user);
			md.setViewName("transfer");
			return "redirect:/transfer";
		} else {
			throw new Exception("Error occured : it seems you are not logged in.");
		}
	}

	@Transactional
	@PostMapping("/contact_action")
	public String contactAction(@RequestParam(value = "connection", required = true) String connectionAsString,
			ModelAndView md) throws Exception {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			User userPrincipal = userService.findByUserName(((UserDetails) principal).getUsername());
			User connection = userService.findByUserName(connectionAsString);

			if (connection != null) {
				if (!transactionServiceImpl.verifyIsAContact(connection, userPrincipal)) {
					entityManager.createNativeQuery("INSERT INTO contacts (first_user, second_user) VALUES (?,?)")
							.setParameter(1, userPrincipal.getId()).setParameter(2, connection.getId()).executeUpdate();
					md.addObject("user", userPrincipal);
					md.addObject("connection", connection);
					md.setViewName("contact");
					return "redirect:/contact";
				} else {
					throw new Exception("You already have this user as a connection.");
				}
			} else {
				throw new Exception("This user do not exist.");
			}
		}
		throw new Exception("Error occured : it seems you are not logged in.");
	}

	@Transactional
	@PostMapping("/transferBank_action")
	public String bankTransferAction(@RequestParam double amount, ModelAndView md) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			User user = userService.findByUserName(((UserDetails) principal).getUsername());
			entityManager.createNativeQuery("UPDATE creditCards SET deposit = ? WHERE user_id = ?;")
					.setParameter(1, amount + user.getCreditCard().getDeposit()).setParameter(2, user.getId())
					.executeUpdate();
			md.addObject("user", user);
			md.setViewName("profile");
			return "redirect:/profile";
		}
		throw new Exception("Error occured : it seems you are not logged in.");
	}
}
