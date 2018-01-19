package com.lonconto.instagraph.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity // check filtre les urls => active certains filtre spring security
@EnableGlobalMethodSecurity(prePostEnabled=true) // pour les preauthorize pouvoir les utuiliser et les active
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean // evite d'avoir a le redclareer partt  - ajout ds la value stack .... mem
	public PasswordEncoder paswordEncoder() {
		if (myPasswordEncoder == null) myPasswordEncoder = new BCryptPasswordEncoder();
		return myPasswordEncoder;
	}
	
	private PasswordEncoder myPasswordEncoder;
	
	@Autowired
	private MyUserDetailsService myUserDetails;
	
	// lors du login on passe par ici
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetails).passwordEncoder(myPasswordEncoder);
	}

	// on accepte req http et on veux authent basic
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests().antMatchers("/extended_api/auth/**").permitAll()
			.and()
			.httpBasic()
			.and()
			.csrf().disable();
	}
	

}
