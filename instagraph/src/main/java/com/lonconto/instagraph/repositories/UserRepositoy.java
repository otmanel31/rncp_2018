package com.lonconto.instagraph.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lonconto.instagraph.metier.User;

public interface UserRepositoy extends CrudRepository<User, Integer> {
	User findByUsername(String username);
}
