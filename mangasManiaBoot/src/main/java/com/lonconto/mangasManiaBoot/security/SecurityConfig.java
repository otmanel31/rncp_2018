package com.lonconto.mangasManiaBoot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lonconto.mangasManiaBoot.security.MyUserDetailsService;

import lombok.extern.log4j.Log4j;

@Configuration
@EnableWebSecurity // check filtre les urls => active certains filtre spring security
@EnableGlobalMethodSecurity(prePostEnabled=true) // pour les preauthorize /postaut ...  pouvoir les utuiliser et les active
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private PasswordEncoder myPasswordEncoder;
	
	@Bean // evite d'avoir a le redclareer partt  - ajout ds la value stack .... mem
	public PasswordEncoder passwordEncoder() {
		if (this.myPasswordEncoder == null) this.myPasswordEncoder = new BCryptPasswordEncoder();
		return this.myPasswordEncoder;
	}
	
	@Autowired
	private MyUserDetailsService myUserDetails;
	
	// lors du login on passe par ici
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		this.log.info(" **************************  in logged in method");
		auth.userDetailsService(myUserDetails).passwordEncoder(myPasswordEncoder);
	}

	// on accepte req http et on veux authent basic
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/extended_api/auth/**").permitAll()
			.and()
			.httpBasic()
			.and()
			.csrf().disable()
		;
	}
	

}
