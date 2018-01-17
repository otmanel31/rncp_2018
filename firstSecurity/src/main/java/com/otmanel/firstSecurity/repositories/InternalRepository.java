package com.otmanel.firstSecurity.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.otmanel.firstSecurity.metier.Role;
import com.otmanel.firstSecurity.metier.User;

@Service
public class InternalRepository implements IInternalRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	// compte nb user ds la base
	/* (non-Javadoc)
	 * @see com.otmanel.firstSecurity.repositories.IInternalRepository#countUsers()
	 */
	@Override
	@Transactional(readOnly=true)
	public long countUsers() {
		return this.em.createQuery("select count(u) from User as u", Long.class).getSingleResult();
	}
	
	/* (non-Javadoc)
	 * @see com.otmanel.firstSecurity.repositories.IInternalRepository#createRole(java.lang.String)
	 */
	@Override
	@Transactional
	public Role createRole(String rolename) {
		Role  r = new Role(0, rolename);
		this.em.persist(r);
		return r;
	}
	
	/* (non-Javadoc)
	 * @see com.otmanel.firstSecurity.repositories.IInternalRepository#createUser(java.lang.String, java.lang.String, com.otmanel.firstSecurity.metier.Role)
	 */
	@Override
	@Transactional
	public User createUser(String username, String pswd, Role...roles) { // ... autant de role quon veux
		User u = new User(0, username, pswd, true);
		for (Role r: roles) {
			u.getRoles().add(r);
		}
		this.em.persist(u);
		return u;
	}
}
