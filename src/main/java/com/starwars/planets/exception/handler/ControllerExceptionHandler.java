package com.starwars.planets.exception.handler;

import com.starwars.planets.exception.ConflictException;
import com.starwars.planets.exception.DataNotFoundException;
import com.starwars.planets.exception.IntegrationException;
import com.starwars.planets.exception.NotAcceptableException;
import com.starwars.planets.exception.model.AttributeMessage;
import com.starwars.planets.exception.model.ExceptionResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

	private static final String VALIDATION_EXCEPTION_MSG = "Validation Exception";

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handlingConstraintValidationException(ConstraintViolationException e) {
		log.error("Handling ConstraintValidationException");

		String message = e.getConstraintViolations()
				.stream()
				.findFirst()
				.map(ConstraintViolation::getMessage)
				.orElse("");

		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, message);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(NotAcceptableException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handlingNotAcceptableException(NotAcceptableException e) {
		log.error("Handling NotAcceptableException", e);

		ExceptionResponse err = new ExceptionResponse(e.getStatus(), e.getMessage());

		return ResponseEntity.status(e.getStatus())
				.body(err);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error("Handling DataIntegrityViolationException", e);

		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handlingException(Exception e) {
		log.error("Handling Exception", e);

		ExceptionResponse err = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(err);
	}

	@ExceptionHandler(IntegrationException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handlingException(IntegrationException e) {
		log.error("Handling IntegrationException", e);
		ExceptionResponse err = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(err);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex) {

		log.error("Handling MissingServletRequestParameter");

		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(ConflictException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> conflictException(ConflictException e) {
		log.error(e.getMessage());
		ExceptionResponse err = new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(err);
	}

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> dataNotFoundException(DataNotFoundException e) {
		log.error(e);
		ExceptionResponse err = new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(err);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException e) {
		log.error(e);
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

		List<AttributeMessage> attributeMessages = new ArrayList<>();
		for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
			attributeMessages.add(new AttributeMessage(((FieldError) objectError).getField(), objectError.getDefaultMessage()));
		}
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_EXCEPTION_MSG, attributeMessages);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}
}
