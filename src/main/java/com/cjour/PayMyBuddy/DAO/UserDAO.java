package com.cjour.PayMyBuddy.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cjour.PayMyBuddy.entity.*;

@Repository
public interface UserDAO extends CrudRepository<User, Integer>{

	Iterable<User> findAll();
	
}
