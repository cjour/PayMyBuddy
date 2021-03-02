package com.cjour.PayMyBuddy.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cjour.PayMyBuddy.DAO.UserDAO;
import com.cjour.PayMyBuddy.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserDAO userDAO;
	
	
	@RequestMapping("/login")
	public String displayLoginForm() {
		return "login";
	}
	
	@RequestMapping("/home")
	public String displayDashboard() {
		return "home";
	}
	
	@GetMapping(value="Users")
	public Iterable<User> listAll(){
		return userDAO.findAll();		
	}
	
	@GetMapping(value="/Users/userName/{userName}")
	public Iterable<User> listUserByUserName(@PathVariable String userName, Model model) {
		model.addAttribute("users", userDAO.findByUserName(userName));
		return null;
	}
	
	@GetMapping(value="/Users/id/{id}")
	public Optional<User> listUserById(@PathVariable Integer id) {
		return userDAO.findById(id);		
	}
}
