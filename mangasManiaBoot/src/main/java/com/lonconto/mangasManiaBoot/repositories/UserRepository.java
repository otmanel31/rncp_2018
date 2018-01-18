package com.lonconto.mangasManiaBoot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lonconto.mangasManiaBoot.metier.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsername(String username);
}
