package com.cjour.PayMyBuddy.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@RequestMapping(value="/Users", method = RequestMethod.GET)
	public String listeUser() {
		return "Un exemple de User";		
	}
}
