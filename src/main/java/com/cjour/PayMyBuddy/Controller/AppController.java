package com.cjour.PayMyBuddy.Controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cjour.PayMyBuddy.DAO.UserRepository;
import com.cjour.PayMyBuddy.entity.User;

@Controller
public class AppController {

	@Autowired
	private UserRepository userService;

	@GetMapping("")
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
		
		userService.save(user);
		
		return "register_success";
	}

}
