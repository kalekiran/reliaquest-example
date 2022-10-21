package com.example.rqchallenge.client.exception.resolver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.rqchallenge.client.exception.ApiServiceException;
import com.example.rqchallenge.client.exception.IntraApiException;
import com.example.rqchallenge.constants.AppConstants;

@RestControllerAdvice
public class ApiExceptionResolver {

	@ExceptionHandler(value = ApiServiceException.class)
	public ResponseEntity<ErrorResponse> exception(ApiServiceException exception) {

		ErrorResponse response = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = IntraApiException.class)
	public ResponseEntity<ErrorResponse> exception(IntraApiException exception) {

		ErrorResponse response = new ErrorResponse(AppConstants.DUMMY_API_ERROR_COE, exception.getErrorMsg());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
