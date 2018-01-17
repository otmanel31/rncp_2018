package com.otmanel.firstSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity // check filtre les urls => active certains filtre spring security
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	// va recup tte les classe implementant UserDetailsService
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean // evite d'avoir a le redclareer partt  - ajout ds la value stack .... mem
	public PasswordEncoder paswordEncoder() {
		if (myPasswordEncoder == null) myPasswordEncoder = new BCryptPasswordEncoder();
		return myPasswordEncoder;
	}
	
	
	private PasswordEncoder myPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// gestion des users en memeoire
		/*auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "USER").and()
			.withUser("toto").password("titi").roles("USER")
			.and().withUser("ellon").password("marslove").roles("VISITOR")			
		;*/
		
		// gestion des users en bdd
			// VIA JDBC Authentication basique 
			// auth.jdbcAuthentication().usersByUsernameQuery('sql ....') // username, pswd enabled
			//	.authoritiesByUsernameQuery(query) // usertname, authority.
		// on veut recuperer nos user et role via jpa
		// Pourcela fournir notre propre sevice de recuperation des objets des utilidateurs
		
		// auth.userDetailsService(userDetailsService)
		auth.userDetailsService(this.userDetailsService)
			.passwordEncoder(this.myPasswordEncoder); // ATTENTION a ne pas faire en vrai (new plaintextencoder .... 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// lobjet http security passer en parametre permet de configurer les doit dacces et plein deutre choses(
		// gestion du login, cors, csrf ....
		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/client").hasAnyRole("ADMIN", "USER")
			.antMatchers("/public").authenticated()
			.antMatchers("/").permitAll()
			.and()
			.httpBasic().and().csrf().disable();
	}

}
