package com.cjour.PayMyBuddy.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjour.PayMyBuddy.Entity.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUserName(String username);
	
}
