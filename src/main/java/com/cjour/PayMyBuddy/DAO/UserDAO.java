package com.cjour.PayMyBuddy.DAO;

import java.util.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cjour.PayMyBuddy.entity.User;

@Repository
public interface UserDAO extends CrudRepository<User, Integer>{

	Collection<User> findAllById(Integer id);
	
}
