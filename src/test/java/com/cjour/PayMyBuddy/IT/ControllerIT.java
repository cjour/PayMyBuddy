package com.cjour.PayMyBuddy.IT;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.cjour.PayMyBuddy.Entity.User;
import com.cjour.PayMyBuddy.Service.UserRepository;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource({ "/application-test.properties" })
@Transactional
public class ControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	SecurityContextHolder securityContextHolder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Test
	public void testGetHome() throws Exception {
		mockMvc.perform(get("/")).andExpect(view()
								 .name("home"))
								 .andExpect(status().isOk());
	}
	
	@Test
	public void testGetRegister() throws Exception {
		mockMvc.perform(get("/register")).andExpect(view()
				 						 .name("register"))
				 						 .andExpect(status().isOk());
	}
		
	@Test
	public void testGetLogin() throws Exception {
		mockMvc.perform(get("/login")).andExpect(view()
				 					  .name("login"))
				 					  .andExpect(status().isOk());;
	}
	
	@WithMockUser(username = "Jean", password = "Jean")
	@Test
	public void transferViewTest() throws Exception {
		mockMvc.perform(get("/transfer")).andExpect(view()
										.name("transfer"))
										.andExpect(status().isOk());
	}

	@Test
	public void userIsWellPopulatedInDatabase() throws Exception {
		// GIVEN
		String userName = "Clément";
		String password = "Clément";

		// WHEN
		mockMvc.perform(post("/register_action").param("userName", userName).param("password",
				passwordEncoder.encode(password))).andExpect(status().isOk());

		// THEN
		assertTrue(userRepository.findByUserName(userName).getUserName().equals(userName));
		assertTrue(passwordEncoder.matches(userRepository.findByUserName(userName).getUserName(),
				passwordEncoder.encode(password)));
	}

	@Test
	public void userIsNotWellPopulatedInDatabase() throws Exception {
		// GIVEN
		String userName = "Jean";
		String password = "Jean";

		// WHEN
		Exception exception = assertThrows(Exception.class,
				() -> mockMvc.perform(post("/register_action").param("userName", userName).param("password",
						passwordEncoder.encode(password))).andExpect(status().isOk()));

		// THEN
		String expectedMessage = "This username is already used, please choose another one.";
		String actualMessage = exception.getMessage();

		// THEN
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void userIsWellFoundInDatabaseAfterInsertion() throws Exception {
		// GIVEN
		String userName = "Mario";
		String password = "Mario";
		mockMvc.perform(post("/login_action").param("userName", userName).param("password",
				new BCryptPasswordEncoder().encode(password)));

		// WHEN
		User result = userRepository.findByUserName(userName);

		// THEN
		assertTrue(result != null);
		assertTrue(result.getUserName().equals(userName));
	}
	
	@Test
	@WithMockUser(username = "Jean", password = "Jean")
	public void contactTest() throws Exception {
		mockMvc.perform(post("/contact_action").param("connection", "Mouchette"))
										.andExpect(redirectedUrl("/contact"));
	}
	
	@Test
	@WithMockUser(username = "Jean", password = "Jean")
	public void transferTest() throws Exception {
		mockMvc.perform(post("/transfer").param("beneficiary", "Mario")
										 .param("description", "Restaurant Saint Valentin")
										 .param("amount", "1.00"))
										.andExpect(redirectedUrl("/transfer"));
	}
	

	@Test
	@WithMockUser(username = "Jean", password = "Jean")
	public void transferBanktest() throws Exception {
		mockMvc.perform(post("/transferBank_action").param("amount", "5000")).andExpect(status().isFound());
	}

	@Test
	@WithMockUser(username = "Jean", password = "Jean")
	public void profileTest() throws Exception {
		mockMvc.perform(get("/profile")).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "Jean", password = "Jean")
	public void contactGetTest() throws Exception {
		mockMvc.perform(get("/contact")).andExpect(status().isOk());
	}
}
