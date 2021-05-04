package com.cjour.PayMyBuddy.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.cjour.PayMyBuddy.Entity.User;

@Service
public class LoginService {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	public UsernamePasswordAuthenticationToken generateToken(User user) {
		return  new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
	}
	
	public void authenticateUser(HttpServletRequest req, User user) {
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(authenticationManager.authenticate(this.generateToken(user)));
		HttpSession session = req.getSession(true);
	    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
	}
	
	public void registerUser(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	
}
