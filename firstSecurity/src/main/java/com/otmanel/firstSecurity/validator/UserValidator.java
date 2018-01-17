package com.otmanel.firstSecurity.validator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.otmanel.firstSecurity.metier.User;

@Component(value="beforeCreateUserValidator")
public class UserValidator implements Validator {

	private static Logger log = LogManager.getLogger(UserValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		log.info(" *********************** in suppports");
		// je ne valide que les entities de classe user
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		log.info(" *********************** validation de l objet : ====> " + target);
		User u = (User)target;
		String name = u.getUsername();
		if (name == null || name.length() > 100 || name.length()<3)
			errors.rejectValue("username", "name is invalid: empty or too short or too long");
		
	}

}
