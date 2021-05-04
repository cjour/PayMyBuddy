package com.cjour.PayMyBuddy.Service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.cjour.PayMyBuddy.Entity.CreditCard;
import com.cjour.PayMyBuddy.Entity.Transaction;
import com.cjour.PayMyBuddy.Entity.User;

@SpringBootTest
public class TransactionServiceTest {

	@InjectMocks
	private static TransactionServiceImpl transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	private static User user;

	@BeforeAll
	public static void setUp() {
		new TransactionServiceImpl();
		transactionService = new TransactionServiceImpl();
		user = new User();
		user.setCreditCard(new CreditCard(1, 500, null));
	}


	@Test
	public void fareCalculationTest() {
		// GIVEN
		double amount = 500;

		// WHEN
		double actual = transactionService.fareCalculation(500);

		// THEN
		double expected = amount + amount * TransactionServiceImpl.COMISSION_PERCENTAGE;
		assertEquals(expected, actual);
	}

//	@Test
//	public void verifySufficientBalanceTest_When_BalanceIsSufficient() {
//		// GIVEN
//		TransactionServiceTest.user.setUserName("Clément");
//		TransactionServiceTest.user.setBalance(500);
//
//		// WHEN
//		boolean actual = transactionService.verifySufficientBalance(TransactionServiceTest.user, 250);
//
//		// THEN
//		assertTrue(actual);
//	}

//	@Test
//	public void verifySufficientBalanceTest_When_BalanceIsInsufficient() {
//		// GIVEN
//		TransactionServiceTest.user.setUserName("Clément");
//		TransactionServiceTest.user.setBalance(500);
//
//		// WHEN
//		boolean actual = transactionService.verifySufficientBalance(TransactionServiceTest.user, 501);
//
//		// THEN
//		assertFalse(actual);
//	}

	@Test
	public void verifyIsAContactTest() {
		// GIVEN
		User c1 = new User();
		c1.setUserName("Michel");
		Set<User> contacts = new HashSet<>();
		contacts.add(c1);
		TransactionServiceTest.user.setContacts(contacts);

		// WHEN
		boolean actual = transactionService.verifyIsAContact(c1, TransactionServiceTest.user);

		// GIVEN
		assertTrue(actual);
	}

	@Test
	public void verifyIsAContactTest_ShouldReturnFalse() {
		// GIVEN
		User c1 = new User();
		c1.setUserName("Michel");
		Set<User> contacts = new HashSet<>();
		TransactionServiceTest.user.setContacts(contacts);

		// WHEN
		boolean actual = transactionService.verifyIsAContact(c1, TransactionServiceTest.user);

		// THEN
		assertFalse(actual);
	}

	@Test
	public void transferTest_Should_ReturnTrue() throws Exception {
		// GIVEN

		TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
		double amount = 25.00;
		User transmittor = new User();
		transmittor.setCreditCard(new CreditCard(1, 26.00, null));
		transmittor.setUserName("Michel");
		User beneficiary = new User();
		beneficiary.setUserName("Chistine");
		Set<User> contacts = new HashSet<>();
		contacts.add(beneficiary);
		transmittor.setContacts(contacts);
		beneficiary.setCreditCard(new CreditCard(2, 25.00, null));
		when(transactionRepository.save(org.mockito.ArgumentMatchers.any())).thenReturn(null);

		// WHEN
		Boolean result = transactionService.transfer(transmittor, beneficiary, "restaurant", amount);

		// THEN
		assertTrue(result);
	}

	@Test
	public void transferTest_ShouldReturn_ExceptionUnsufficientBalance() throws Exception {
		// GIVEN
		double amount = 26.00;
		User transmittor = new User();
		transmittor.setCreditCard(new CreditCard(1, 20.00, null));
		transmittor.setUserName("Michel");
		User beneficiary = new User();
		beneficiary.setUserName("Chistine");
		Set<User> contacts = new HashSet<>();
		contacts.add(beneficiary);
		transmittor.setContacts(contacts);
		beneficiary.setCreditCard(new CreditCard(2, 25.00, null));

		// WHEN
		Exception exception = assertThrows(Exception.class, () -> {
			transactionService.transfer(transmittor, beneficiary, "restaurant", amount);
		});

		String expectedMessage = "Your balance is not sufficient for this transfer";
		String actualMessage = exception.getMessage();

		// THEN
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	public void transferTest_ShouldReturn_ExceptionUnknownContact() throws Exception {
		// GIVEN
		double amount = 26.00;
		User transmittor = new User();
		transmittor.setCreditCard(new CreditCard(1, 27.00, null));
		transmittor.setUserName("Michel");
		User beneficiary = new User();
		beneficiary.setUserName("Chistine");
		Set<User> contacts = new HashSet<>();
		transmittor.setContacts(contacts);
		beneficiary.setCreditCard(new CreditCard(2, 25.00, null));

		// WHEN
		Exception exception = assertThrows(Exception.class, () -> {
			transactionService.transfer(transmittor, beneficiary, "restaurant", amount);
		});

		String expectedMessage = "You do not have this contact in your friends directory";
		String actualMessage = exception.getMessage();

		// THEN
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void transferTest_ShouldReturn_ExceptionAmountNegativeOrEqualsZero() throws Exception {
		// GIVEN
		double amount = 0;
		User transmittor = new User();
		transmittor.setCreditCard(new CreditCard(1, 27.00, null));
		transmittor.setUserName("Michel");
		User beneficiary = new User();
		beneficiary.setUserName("Chistine");
		Set<User> contacts = new HashSet<>();
		transmittor.setContacts(contacts);
		beneficiary.setCreditCard(new CreditCard(2, 25.00, null));

		// WHEN
		Exception exception = assertThrows(Exception.class, () -> {
			transactionService.transfer(transmittor, beneficiary, "restaurant", amount);
		});

		String expectedMessage = "Amount must be stricly superior to 0";
		String actualMessage = exception.getMessage();

		// THEN
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void getAllTransactionsTest() {
		// GIVEN
		
		User jean = new User();
		jean.setUserName("Jean");
		
		List<Transaction> transactionAsTransmittor = new ArrayList<>();
		transactionAsTransmittor.add(new Transaction(jean, 1, 20.00, "café à Paris", user));
		user.setTransactions(transactionAsTransmittor);
		
		List<Transaction> transactionAsBeneficiary = new ArrayList<>();
		transactionAsBeneficiary.add(new Transaction(user, 1, 1.00, "café à Orléans", jean));
		user.setTransactionsAsTransmittor(transactionAsTransmittor);


		// WHEN
		List<Transaction> merged = transactionService.getAllTransactionsByUser(user);

		// THEN
		assertTrue(merged.size() == 2);
		
	}
}
