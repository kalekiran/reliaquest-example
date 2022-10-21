package com.example.rqchallenge.service;

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
import com.example.rqchallenge.dto.ResourceData;
import com.example.rqchallenge.dto.ResourceList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author dkale
 * 
 * Service class which calls APIs using RestClient and provides the data.
 *
 */
@Service
public class EmployeeService {

	@Autowired
	private RestClient restClient;
	
	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	public ResponseEntity<ResourceList<Employee>> getAllEmployees() throws IOException {
		ResourceList<Employee> empList = getEmployeeList();
		return new ResponseEntity<>(empList, HttpStatus.OK);
	}

	public ResponseEntity<ResourceData<Employee>> getEmployeeById(Integer id) {
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_BY_ID_URL);
        urlBuilder.append(AppConstants.PATH_SEPERATOR).append(id);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		
		ResponseDetails<DummyApiIdResponse> response = restClient.execute(requestDetails, DummyApiIdResponse.class);
		ResourceData<Employee> resourceData = buildIdResponse(response ,id);
		return new ResponseEntity<>(resourceData, HttpStatus.OK);
	}
	
	public ResponseEntity<DummyApiOperationResponse> createEmployee(Map<String, Object> employeeInput) {
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_CREATE_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.POST, null, httpHeaders, employeeInput);
		
		ResponseDetails<DummyApiOperationResponse> response = restClient.execute(requestDetails, DummyApiOperationResponse.class);
		if (response == null) {
			throw new ApiServiceException("Error occured while creating Employee with payload :." + employeeInput);
		}
		return new ResponseEntity<>(response.getResponseBody(), HttpStatus.OK);
	}

	public ResponseEntity<DummyApiDeleteResponse> deleteEmployee(String id) {
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_DELETE_URL);
        urlBuilder.append(AppConstants.PATH_SEPERATOR).append(id);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.DELETE, null, httpHeaders);
		
		ResponseDetails<DummyApiDeleteResponse> response = restClient.execute(requestDetails, DummyApiDeleteResponse.class);
		if (response == null) {
			throw new ApiServiceException("Error occured while deleting Employee with id :." + id);
		}
		return new ResponseEntity<>(response.getResponseBody(), HttpStatus.OK);
	}

	public ResponseEntity<ResourceList<Employee>> getEmployeesByNameSearch(String searchString) {
		List<Employee> empList = getEmployeeList().getData();
		List<Employee> filterList = empList.stream()
		       .filter(emp -> emp.getEmployee_name().contains(searchString))
		       .collect(Collectors.toList());
		ResourceList<Employee> reslist = new ResourceList<>(filterList);
		return new ResponseEntity<>(reslist, HttpStatus.OK);
	}

	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		List<Employee> empList = getEmployeeList().getData();
				Optional<Integer> maxSalary = empList.stream()
		       .map(Employee::getEmployee_salary)
		       .max(Integer::compare);
		
		if(maxSalary.isPresent()) {
			return new ResponseEntity<>(maxSalary.get(), HttpStatus.OK);
		}
		
		throw new ApiServiceException("Error occured while fetching highest salary of Employees.");
	}

	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		List<Employee> empList = getEmployeeList().getData();
		List<String> empNames = empList.stream()
				.sorted((o1, o2) -> (o2.getEmployee_salary().compareTo(o1.getEmployee_salary())))
				.limit(10)
				.map(Employee::getEmployee_name)
				.collect(Collectors.toList());
		
		return new ResponseEntity<>(empNames, HttpStatus.OK);
	}
	
	private ResourceList<Employee> getEmployeeList() {
		StringBuilder urlBuilder = new StringBuilder(AppConstants.DUMMY_EMP_GET_ALL_URL);
		HttpHeaders httpHeaders = new HttpHeaders();
		RequestDetails requestDetails = new RequestDetails(urlBuilder.toString(), HttpMethod.GET, null, httpHeaders);
		
		ResponseDetails<DummyApiResponse> response = restClient.execute(requestDetails, DummyApiResponse.class);
		return buildResponse(response);
	}
	
	private ResourceList<Employee> buildResponse(ResponseDetails<DummyApiResponse> response) {
		if (response == null) {  // mock response data
			return new ResourceList<>(mockEmployeeData());
		}
		
		if (response.getStatus() != HttpStatus.OK) {
			logger.error("Error occured while parsing the response with status code:" , response.getStatus());
			throw new ApiServiceException("Error occured while fetching Employees.");
		}
		List<Employee> empList = response.getResponseBody().getData();
		return new ResourceList<>(empList);
	}

	private ResourceData<Employee> buildIdResponse(ResponseDetails<DummyApiIdResponse> response, Integer id) {
		if (response == null) {  // mock data
			return mockDataForEmployeeById(id);
		}

		if (response.getStatus() != HttpStatus.OK) {
			logger.error("Error occured while parsing the response with status code:" , response.getStatus());
			throw new ApiServiceException("Error occured while fetching Employee with Id:." + id);
		}
		Employee emp = response.getResponseBody().getData();
		return new ResourceData<>(emp);
	}

	private List<Employee> mockEmployeeData() {
		InputStream inJson = DummyApiResponse.class.getResourceAsStream(AppConstants.MOCK_DATA_FILE);
		DummyApiResponse response = null;
		try {
			response = new ObjectMapper().readValue(inJson, DummyApiResponse.class);
		} catch (IOException e) {
			logger.error("Error occured while reading the input mock data file.");
			response = new DummyApiResponse();
		}
		return response.getData();
	}
	
	private ResourceData<Employee> mockDataForEmployeeById(int id) {
		List<Employee> empList = mockEmployeeData();
		Optional<Employee> employee = empList.stream()
		        .filter(emp -> emp.getId().equals(id))
		        .findFirst();
		if(employee.isPresent()) {
			return new ResourceData<>(employee.get());
		}
		throw new ApiServiceException("Error occured while fetching Employee with id :." + id);
	}

}
