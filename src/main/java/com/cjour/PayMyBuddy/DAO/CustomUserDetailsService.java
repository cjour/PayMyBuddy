package com.cjour.PayMyBuddy.DAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import com.cjour.PayMyBuddy.entity.User;



public class CustomUserDetailsService implements UserDetailsService {

		@Autowired
		private UserRepository userRepo;
		
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User user = userRepo.findByUserName(username);
			if (user == null) {
				throw new UsernameNotFoundException("User not found");
			}
			return new CustomUserDetails(user);
		}

	}