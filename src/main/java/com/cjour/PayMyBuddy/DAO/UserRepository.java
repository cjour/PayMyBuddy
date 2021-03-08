package com.cjour.PayMyBuddy.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjour.PayMyBuddy.entity.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUserName(String username);
	
}
