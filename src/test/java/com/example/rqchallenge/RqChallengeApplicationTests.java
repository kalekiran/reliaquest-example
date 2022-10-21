package com.example.rqchallenge;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.rqchallenge.client.RequestDetails;
import com.example.rqchallenge.client.ResponseDetails;
import com.example.rqchallenge.client.RestClient;
import com.example.rqchallenge.client.exception.ApiServiceException;
import com.example.rqchallenge.constants.AppConstants;
import com.example.rqchallenge.dto.DummyApiDeleteResponse;
import com.example.rqchallenge.dto.DummyApiIdResponse;
import com.example.rqchallenge.dto.DummyApiOperationResponse;
import com.example.rqchallenge.dto.DummyApiResponse;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class RqChallengeApplicationTests {

	@Autowired
	private EmployeeService employeeService;
	
	@MockBean
	private RestClient restClient;
	
	Logger logger = LoggerFactory.getLogger(RqChallengeApplicationTests.class);
	
	@Test
    void getEmployeeList() throws IOException {
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_ALL_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		when(restClient.execute(requestDetails, DummyApiResponse.class)).thenReturn(mockEmployeeDataResponse());
		
		int empListSize = 24;
		
		assertNotNull(employeeService.getAllEmployees().getBody());
		assertNotNull(employeeService.getAllEmployees().getBody().getData());
		int returnListSize = employeeService.getAllEmployees().getBody().getData().size();
		assertEquals(empListSize, returnListSize);
    }
	
	@Test
    void getEmployeeById() {
		int id = 5;
		
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_BY_ID_URL);
        urlBuilder.append(AppConstants.PATH_SEPERATOR).append(id);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		when(restClient.execute(requestDetails, DummyApiIdResponse.class)).thenReturn(mockEmployeeByIdResponse());
		
		assertNotNull(employeeService.getEmployeeById(id).getBody());
		Employee returnEmp = employeeService.getEmployeeById(id).getBody().getData();
		assertNotNull(returnEmp);
		assertEquals(id, returnEmp.getId());
    }
	
	@Test
    void getEmployeesByNameSearch() {
		String searchString = "Nixon";
		
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_ALL_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		when(restClient.execute(requestDetails, DummyApiResponse.class)).thenReturn(mockEmployeeDataResponse());
		
		assertNotNull(employeeService.getEmployeesByNameSearch(searchString).getBody());
		List<Employee> returnEmpList = employeeService.getEmployeesByNameSearch(searchString).getBody().getData();
		assertNotNull(returnEmpList);
		assertEquals(1, returnEmpList.size());
		assertTrue(returnEmpList.get(0).getEmployee_name().contains(searchString));
    }
	
	@Test
    void getHighestSalaryOfEmployees() {
		Integer highestSalary = 725000;
		
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_ALL_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		when(restClient.execute(requestDetails, DummyApiResponse.class)).thenReturn(mockEmployeeDataResponse());
		
		assertNotNull(employeeService.getHighestSalaryOfEmployees());
		Integer maxSalary = employeeService.getHighestSalaryOfEmployees().getBody();
		assertNotNull(maxSalary);
		assertEquals(highestSalary, maxSalary);
    }
	
	@Test
    void getTopTenHighestEarningEmployeeNames() {
		List<String> list = Arrays.asList("Paul Byrd", "Yuri Berry", "Charde Marshall", "Cedric Kelly", "Tatyana Fitzpatrick",
				                          "Brielle Williamson", "Jenette Caldwell", "Quinn Flynn", "Rhona Davidson", "Tiger Nixon");
		
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_ALL_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		when(restClient.execute(requestDetails, DummyApiResponse.class)).thenReturn(mockEmployeeDataResponse());
		
		assertNotNull(employeeService.getTopTenHighestEarningEmployeeNames());
		List<String> empNamesList = employeeService.getTopTenHighestEarningEmployeeNames().getBody();
		assertNotNull(empNamesList);
		assertTrue(empNamesList.size() == 10);
		assertEquals(empNamesList, list);
		
    }
	
	@Test
    void createEmployee() {
		//{"name":"test-9","salary": "15" ,"age":"24"}
		Map<String, Object> employeeInput = new HashMap<>();
		employeeInput.put("name", "test-9");
		employeeInput.put("salary", "15");
		employeeInput.put("age", "24");
		
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_CREATE_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.POST, null, httpHeaders, employeeInput);
		when(restClient.execute(requestDetails, DummyApiOperationResponse.class)).thenReturn(mockCreateEmployeeDataResponse(employeeInput));
		
		try {
			ResponseEntity<DummyApiOperationResponse>  restResponse = employeeService.createEmployee(employeeInput);
			assertNotNull(restResponse);
			DummyApiOperationResponse response = restResponse.getBody();
			assertNotNull(response);
			assertEquals(response.getData().get("name"), employeeInput.get("name"));
			assertEquals(response.getData().get("salary"), employeeInput.get("salary"));
			assertEquals(response.getData().get("age"), employeeInput.get("age"));
		} catch (ApiServiceException ex) {
			logger.error("Error occured while creating Employee with payload :." + employeeInput);
		}
    }
	

	@Test
    void deleteEmployee() {
		String id  = "5";
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_DELETE_URL);
        urlBuilder.append(AppConstants.PATH_SEPERATOR).append(id);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.DELETE, null, httpHeaders);
		when(restClient.execute(requestDetails, DummyApiDeleteResponse.class)).thenReturn(mockDeleteEmployeeDataResponse(id));
		
		try {
			ResponseEntity<DummyApiDeleteResponse>  restResponse = employeeService.deleteEmployee(id);
			assertNotNull(restResponse);
			DummyApiDeleteResponse response = restResponse.getBody();
			assertNotNull(response);
			assertEquals(response.getData(), id);
		} catch (ApiServiceException ex) {
			logger.error("Error occured while deleting Employee with id :." + id);
		}
    }
	
	private ResponseDetails<DummyApiResponse> mockEmployeeDataResponse() {
		DummyApiResponse response = mockEmployeeData();
		return new ResponseDetails<>(HttpStatus.OK, new HttpHeaders() ,response, null);
	}
	
	private ResponseDetails<DummyApiOperationResponse> mockCreateEmployeeDataResponse(Map<String, Object> employeeInput) {
		DummyApiOperationResponse response = new DummyApiOperationResponse();
		response.setData(employeeInput);
		return new ResponseDetails<>(HttpStatus.OK, new HttpHeaders() ,response, null);
	}
	
	private ResponseDetails<DummyApiDeleteResponse> mockDeleteEmployeeDataResponse(String id) {
		DummyApiDeleteResponse response = new DummyApiDeleteResponse();
		response.setData(id);
		return new ResponseDetails<>(HttpStatus.OK, new HttpHeaders() ,response, null);
	}
	
	private ResponseDetails<DummyApiIdResponse> mockEmployeeByIdResponse() {
		DummyApiResponse response = mockEmployeeData();
		Employee emp = response.getData().get(4);
		
		DummyApiIdResponse result = new DummyApiIdResponse();
		result.setData(emp);
		
		return new ResponseDetails<>(HttpStatus.OK, new HttpHeaders() ,result, null);
	}
	
	private DummyApiResponse mockEmployeeData() {
		InputStream inJson = DummyApiResponse.class.getResourceAsStream(AppConstants.MOCK_DATA_FILE);
		DummyApiResponse response = null;
		try {
			response = new ObjectMapper().readValue(inJson, DummyApiResponse.class);
		} catch (IOException e) {
			logger.error("Error occured while reading the input mock data file.");
			response = new DummyApiResponse();
		}
		return response;
	}

}
