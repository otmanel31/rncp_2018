package com.otmanel.firstSecurity.repositories;

import org.springframework.transaction.annotation.Transactional;

import com.otmanel.firstSecurity.metier.Role;
import com.otmanel.firstSecurity.metier.User;

public interface IInternalRepository {

	// compte nb user ds la base
	long countUsers();

	Role createRole(String rolename);

	User createUser(String username, String pswd, Role... roles);

}