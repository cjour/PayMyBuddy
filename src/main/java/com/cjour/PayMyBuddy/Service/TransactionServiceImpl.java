package com.cjour.PayMyBuddy.Service;

import org.springframework.transaction.annotation.Transactional;

import com.cjour.PayMyBuddy.Entity.Transaction;
import com.cjour.PayMyBuddy.Entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

	public static final double COMISSION_PERCENTAGE = 0.005;

	@Autowired
	UserRepository userService;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@PersistenceContext
    private EntityManager entityManager;

	
	public double fareCalculation(double amount) {
		return amount + (amount * COMISSION_PERCENTAGE);
	}

	
	public boolean verifySufficientBalance(User user, double amount) {
		if (user.getCreditCard().getDeposit() > fareCalculation(amount)) {
			return true;
		}
		return false;
	}

	
	public boolean verifyIsAContact(User beneficiary, User transmittor) {
		for (User user : transmittor.getContacts()) {
			if (user.getUserName().equals(beneficiary.getUserName())) {
				return true;
			}
		}
		return false;
	}

	@Transactional
	public boolean transfer(User transmittor, User beneficiary, String description, double amount) throws Exception {
		if (amount > 0) {
			if (verifySufficientBalance(transmittor, amount)) {
				if(verifyIsAContact(beneficiary, transmittor)){
					Transaction currentTransaction = new Transaction();
					currentTransaction.setAmount(amount);
					currentTransaction.setDescription(description);
					currentTransaction.setTransmittor(transmittor);
					currentTransaction.setUser(beneficiary);
					transactionRepository.save(currentTransaction);
					transmittor.getCreditCard().setDeposit((transmittor.getCreditCard().getDeposit() - fareCalculation(amount)));
					beneficiary.getCreditCard().setDeposit(beneficiary.getCreditCard().getDeposit() + amount);
					return true;
				}else{
					throw new Exception("You do not have this contact in your friends directory");
				}
			}else{
				throw new Exception("Your balance is not sufficient for this transfer");
			}
		}else{
			throw new Exception("Amount must be stricly superior to 0");
		}		
	}
	
	public List<Transaction> getAllTransactionsByUser(User user) {
		List<Transaction> transactions1 = user.getTransactions();
		List<Transaction> transactions2 = user.getTransactionsAsTransmittor();		
		List<Transaction> merged = new ArrayList<Transaction>(transactions1);
        merged.addAll(transactions2);
        
        return merged;
	}
}
