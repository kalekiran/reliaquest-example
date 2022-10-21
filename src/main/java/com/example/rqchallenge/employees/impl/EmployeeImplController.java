package com.example.rqchallenge.employees.impl;

import com.example.rqchallenge.dto.DummyApiDeleteResponse;
import com.example.rqchallenge.dto.DummyApiOperationResponse;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.ResourceData;
import com.example.rqchallenge.dto.ResourceList;
import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.service.EmployeeService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author dkale
 * 
 * API Implementation class which provides the APIs related with Employee resource.
 *
 */
@Component
public class EmployeeImplController implements IEmployeeController {

	Logger logger = LoggerFactory.getLogger(EmployeeImplController.class);
	
	@Autowired
	private EmployeeService employeeService;

	
	@Override
	public ResponseEntity<ResourceList<Employee>> getAllEmployees() throws IOException {
		logger.debug(":getAllEmployees() -");
		return employeeService.getAllEmployees();
	}

	@Override
	public ResponseEntity<ResourceList<Employee>> getEmployeesByNameSearch(String searchString) {
		logger.debug(":getEmployeesByNameSearch() - search String -" + searchString );
		return employeeService.getEmployeesByNameSearch(searchString);
	}

	@Override
	public ResponseEntity<ResourceData<Employee>> getEmployeeById(Integer id) {
		logger.debug(":getEmployeeById() - id -" + id );
		return employeeService.getEmployeeById(id);
	}

	@Override
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		logger.debug(":getHighestSalaryOfEmployees() -");
		return employeeService.getHighestSalaryOfEmployees();
	}

	@Override
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		logger.debug(":getTopTenHighestEarningEmployeeNames() -");
		return employeeService.getTopTenHighestEarningEmployeeNames();
	}

	@Override
	public ResponseEntity<DummyApiOperationResponse> createEmployee(Map<String, Object> employeeInput) {
		logger.debug(":createEmployee() -");
		return employeeService.createEmployee(employeeInput);
	}

	@Override
	public ResponseEntity<DummyApiDeleteResponse> deleteEmployeeById(String id) {
		logger.debug(":deleteEmployeeById() - id -" + id );
		return employeeService.deleteEmployee(id);
	}

}
