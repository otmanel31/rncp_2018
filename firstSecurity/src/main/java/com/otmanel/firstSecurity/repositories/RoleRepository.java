package com.otmanel.firstSecurity.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import com.otmanel.firstSecurity.metier.Role;
@PreAuthorize("hasRole('ROLE_USER')")
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {
	@PreAuthorize("permitAll")
	Role findByName(String name);
}
