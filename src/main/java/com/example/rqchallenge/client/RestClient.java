package com.example.rqchallenge.client;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.rqchallenge.client.exception.IntraApiException;
import com.example.rqchallenge.constants.AppConstants;

/**
 * @author dkale
 * 
 * client which invokes Rest APIs and provides the API response using RestTemplate.
 *
 */
@Component
public class RestClient {

	@Autowired
	private RestTemplate restTemplate;
	
	Logger logger = LoggerFactory.getLogger(RestClient.class);

	public <R> ResponseDetails<R> execute(RequestDetails requestDetails, Class<R> response) throws IntraApiException {
		logger.debug("Executing request -" + requestDetails.getUrl());
		
		if (requestDetails == null || response == null) {
			throw new IllegalArgumentException("Invalid request details or invalid response");
		}

		ResponseDetails<R> responseDetails = null;
		try {

			URI encodedURL = prepareURL(requestDetails);
			HttpHeaders headers = prepareRequestHeaders(requestDetails);
			HttpEntity<?> request = new HttpEntity<>(requestDetails.getRequestBody(), headers);

			ResponseEntity<R> responseEntity = restTemplate.exchange(encodedURL, requestDetails.getMethod(), request,
					response);

			if (responseEntity != null) {
				logger.debug("Returned response code from API:" + responseEntity.getStatusCode());
				responseDetails = prepareReponse(responseEntity);
			}
		} catch (HttpStatusCodeException hsce) {
			// Dummy API returned error, Mock the data
			logger.error("Error occured while executing the request:" , requestDetails);
		} catch (RestClientException rce) {
			throw new IntraApiException(HttpStatus.SERVICE_UNAVAILABLE.value(), "Error occured while excecuting the request.");
		}
		return responseDetails;
	}

	private HttpHeaders prepareRequestHeaders(RequestDetails requestDetails) {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		headers.add(HttpHeaders.ACCEPT, "application/json");

		if (requestDetails.getRequestHeaders() != null && requestDetails.getRequestHeaders().size() > 0) {
			headers.putAll(requestDetails.getRequestHeaders());
		}
		return headers;
	}

	private URI prepareURL(RequestDetails requestDetails) {
		String url = AppConstants.DUMMY_API_BASE_URL + requestDetails.getUrl();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

		if (requestDetails.getQueryParams() != null && requestDetails.getQueryParams().size() > 0) {
			builder.queryParams(requestDetails.getQueryParams());
		}

		return builder.build().encode().toUri();
	}

	private <R> ResponseDetails<R> prepareReponse(ResponseEntity<R> responseEntity) {
		return new ResponseDetails<>(responseEntity.getStatusCode(), responseEntity.getHeaders(),
				responseEntity.getBody(), null);
	}

}
