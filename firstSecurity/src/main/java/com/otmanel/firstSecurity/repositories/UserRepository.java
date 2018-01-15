package com.otmanel.firstSecurity.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.firstSecurity.metier.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

}
