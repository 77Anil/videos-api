package com.main.videosapi.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
@RestController
public class ExceptonHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handelAllException(Exception ex, WebRequest request) throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"INTERNAL_SERVER_ERROR", ex.getMessage(),

				ServletUriComponentsBuilder.fromCurrentRequest().path("").build().getPath());
		return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<Object> handelAuthenticationExceptionException(Exception ex, WebRequest request)
			throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED",
				ex.getMessage(),

				ServletUriComponentsBuilder.fromCurrentRequest().path("").build().getPath());
		return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NotFoundExpection.class)
	public final ResponseEntity<Object> handleUserNotFoundException(NotFoundExpection ex, WebRequest request)
			throws Exception {
		ExceptionResponse exception = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
				ex.getMessage(), ServletUriComponentsBuilder.fromCurrentRequest().path("").build().getPath());
		return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exception = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Validation Error", ex.getFieldError().getDefaultMessage(),

				ServletUriComponentsBuilder.fromCurrentRequest().path("").build().getPath());

		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}

}
