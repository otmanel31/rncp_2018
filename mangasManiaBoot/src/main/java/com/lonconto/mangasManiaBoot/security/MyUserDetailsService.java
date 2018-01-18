package com.lonconto.mangasManiaBoot.security;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lonconto.mangasManiaBoot.metier.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TypedQuery<User> q = this.em.createQuery("select u from User as u left join fetch u.roles where u.username=:username ", User.class);
		q.setParameter("username", username);
		User u = q.getSingleResult();
		
		return new MyUserDetails(u);
	}

}
