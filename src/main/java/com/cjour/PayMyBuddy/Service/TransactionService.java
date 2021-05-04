package com.cjour.PayMyBuddy.Service;

import org.springframework.stereotype.Repository;

import com.cjour.PayMyBuddy.Entity.User;

@Repository
public interface TransactionService {

	public boolean transfer(User transmittor, User beneficiary, String description, double amount) throws Exception;
	public double fareCalculation(double amount);
	public boolean verifySufficientBalance(User user, double amount);	
	public boolean verifyIsAContact(User beneficiary, User transmittor);

}
