package com.cjour.PayMyBuddy.controllerIT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
	}
	
	@Test
	public void userIsWellPopulatedInDatabase() throws Exception {
		//GIVEN
		String userName = "Jean";
		String password = "Jean";
		
		//WHEN
		mockMvc.perform(post("/register_action")
					.param("userName", userName)
					.param("password", passwordEncoder.encode(password))
				).andExpect(status().isOk());
		
		//GIVEN
		assertTrue(userRepository.findByUserName(userName).getUserName().equals(userName));
		assertTrue(passwordEncoder.matches(
				userRepository.findByUserName(userName).getUserName(),
				passwordEncoder.encode(password)));
	}
	
	@Test
	public void userIsWellFoundInDatabaseAfterInsertion() throws Exception {
		//GIVEN
		String userName = "Mario";
		String password = "Mario";
		mockMvc.perform(post("/register_action")
				.param("userName", userName)
				.param("password", new BCryptPasswordEncoder().encode(password))
			);
				
		//WHEN
		User result = userRepository.findByUserName(userName);
		
		//GIVEN
		assertTrue(result != null);
		assertTrue(result.getUserName().equals(userName));
	}
}