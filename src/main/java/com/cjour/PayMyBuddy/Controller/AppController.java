package com.cjour.PayMyBuddy.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cjour.PayMyBuddy.DAO.LoginService;
import com.cjour.PayMyBuddy.DAO.UserRepository;
import com.cjour.PayMyBuddy.entity.User;

@Controller
public class AppController {

	@Autowired
	LoginService loginService;
	
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
	public String loginAction(HttpServletRequest req, User user) {
		
	    loginService.authenticateUser(req, user);
	    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
	      return "dashboardUser";
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
	
	@GetMapping("/logout")
	  public String fetchSignoutSite(HttpServletRequest request){    
	      SecurityContextHolder.clearContext();
	      HttpSession session = request.getSession(false);
	      if(session != null) {
	          session.invalidate();
	          return "home";
	      } else {
	    	  return "errorPageLogout";
	      }
	 }
}
