package com.otmanel.firstSecurity.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // conseil appliqeuer a dautre controller
public class RestValidationExceptionHandler extends ResponseEntityExceptionHandler {
	
	// response entity ==> on peux renvoyer a peux pres nimporte koi de ce que lon decide
	@ExceptionHandler({RepositoryConstraintViolationException.class}) // erreur a capturer ici durant la validation
	public ResponseEntity<Object> handleValidationErrorException(Exception ex, WebRequest req){
		RepositoryConstraintViolationException vex = (RepositoryConstraintViolationException)ex;
		
		List<Map<String, String>> errors = vex.getErrors().getFieldErrors().stream().map(e -> {
			HashMap<String, String> message = new HashMap<>();
			message.put("fieldname", e.getField());
			message.put("error", e.getCode());
			message.put("resource", e.getObjectName());
			return message;
		})
		.collect(Collectors.toList())
		;
		return new ResponseEntity<Object>(errors, new HttpHeaders(),  HttpStatus.PARTIAL_CONTENT);
	}
}
