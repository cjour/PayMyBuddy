
  package com.cjour.PayMyBuddy.Service;
  
  import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjour.PayMyBuddy.Entity.Transaction;
  
	@Repository
	public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

  }
 