package com.cjour.PayMyBuddy.controllerIT;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cjour.PayMyBuddy.DAO.UserRepository;
import com.cjour.PayMyBuddy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)

@TestPropertySource({ "/application-test.properties" })
public class ControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@BeforeEach
	public void cleanDatabase() throws SQLException {
		// @Query("DELETE FROM users, transactions, creditcards, contacts")
		// How to just send a query ?
	}

	@Test
	public void userIsWellPopulatedInDatabase() throws Exception {
		//GIVEN
		String userName = "Jean";
		String password = "Jean";

		//WHEN
		mockMvc.perform(post("/register_action").param("userName", userName).param("password",
				passwordEncoder.encode(password))).andExpect(status().isOk());

		//THEN
		assertTrue(userRepository.findByUserName(userName).getUserName().equals(userName));
		assertTrue(passwordEncoder.matches(userRepository.findByUserName(userName).getUserName(),
				passwordEncoder.encode(password)));
	}

	@Test
	public void userIsWellFoundInDatabaseAfterInsertion() throws Exception {
		//GIVEN
		String userName = "Mario";
		String password = "Mario";
		mockMvc.perform(post("/register_action").param("userName", userName).param("password",
				new BCryptPasswordEncoder().encode(password)));

		//WHEN
		User result = userRepository.findByUserName(userName);

		//THEN
		assertTrue(result != null);
		assertTrue(result.getUserName().equals(userName));
	}

//	@Test
//	public void userIsWellLogoutAfterLogOffAction() throws Exception{
//
//		//GIVEN
//		 userIsWellFoundInDatabaseAfterInsertion();
//		 
//		 //WHEN
//		 mockMvc.perform(get("/logout"));
//		 RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//		 ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
//		 HttpServletRequest request = attributes.getRequest();
//		 HttpSession httpSession = request.getSession(true);
//		
//		 
//		 //THEN
//		 assertTrue(httpSession == null);
//		
//	}
}
