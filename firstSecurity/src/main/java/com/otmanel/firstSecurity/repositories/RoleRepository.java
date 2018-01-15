package com.otmanel.firstSecurity.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.otmanel.firstSecurity.metier.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

}
