package com.otmanel.firstSecurity.metier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString(exclude= {"roles", "password"})
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=100, nullable=false, unique=true)
	private String username;
	@Column(length=100)
	private String password;
	private boolean enabled;
	@ManyToMany
	private Set<Role> roles;
	
	public Set<Role> getRoles(){
		if (this.roles == null ) this.roles = new HashSet<>();
		return this.roles;
	}

	public User(int id, String username, String password, boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}
	
	
}
