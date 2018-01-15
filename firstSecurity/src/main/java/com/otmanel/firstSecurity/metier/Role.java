package com.otmanel.firstSecurity.metier;

import java.util.HashSet;
import java.util.Set;

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
@Getter @Setter @NoArgsConstructor @ToString(exclude= {"user"})
public class Role {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	@ManyToMany(mappedBy="roles")
	private Set<User> users;
	
	public Set<User> getUsers(){
		if (this.users == null ) this.users = new HashSet<>();
		return this.users;
	}

	public Role(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
