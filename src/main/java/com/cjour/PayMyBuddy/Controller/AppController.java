package com.cjour.PayMyBuddy.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cjour.PayMyBuddy.DAO.UserRepository;
import com.cjour.PayMyBuddy.entity.User;

@Controller
public class AppController {

	@Autowired
	private UserRepository userService;

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
	public String register(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		if (userService.findByUserName(user.getUserName()) == null) {
			userService.save(user);
			return "register_success";
		}

		return "register";
	}

	@GetMapping("/login")
	public ModelAndView login(ModelAndView md, User user) {
		md.addObject("user", user);
		md.setViewName("login");
		return md;
	}

	@PostMapping("/login_action")
	public String loginAction(User user) {
		User userRetrieved = userService.findByUserName(user.getUserName());
		if (userRetrieved != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(user.getPassword(), userRetrieved.getPassword())) {
				return "dashboardUser";
			}
		}
		return "login";
	}
	
	@GetMapping("/transfer")
	public String transferView() {
		return "transfer";
	}
	
	@GetMapping("/profile")
	public String profileView() {
		return "profile";
	}
	
	@GetMapping("/contact")
	public String contactView() {
		return "contact";
	}
	
	@GetMapping("/logOff")
	public String logOff() {
		return "home";
	}
	
	
}
