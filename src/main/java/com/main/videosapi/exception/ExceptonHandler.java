package com.main.videosapi.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.JwtException;

@ControllerAdvice
@RestController
public class ExceptonHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handelAllException(Exception ex, WebRequest request) throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(JwtException.class)
	public final ResponseEntity<Object> jwtException(JwtException ex, WebRequest request) throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity<Object> badCredential(BadCredentialsException ex, WebRequest request) throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundExpection.class)
	public final ResponseEntity<Object> handleUserNotFoundException(NotFoundExpection ex, WebRequest request)
			throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exception = new ExceptionResponse(new Date(), "Validation Error",
				ex.getFieldError().getDefaultMessage());
		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}
}
