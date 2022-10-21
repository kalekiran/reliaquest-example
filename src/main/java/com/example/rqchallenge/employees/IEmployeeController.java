package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rqchallenge.constants.AppConstants;
import com.example.rqchallenge.dto.DummyApiDeleteResponse;
import com.example.rqchallenge.dto.DummyApiOperationResponse;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.ResourceData;
import com.example.rqchallenge.dto.ResourceList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author dkale
 * Provides the API's related to Employee resource.
 *
 */
@RestController
@RequestMapping(AppConstants.BASE_EMP_SERVICE_URL)
public interface IEmployeeController {

    /**
     * Returns the list of all employees.
     * 
     */
    @GetMapping()
    ResponseEntity<ResourceList<Employee>> getAllEmployees() throws IOException;

    /**
     * @param searchString  search string.
     * @return all employees whose name contains or matches the string input provided.
     */
    @GetMapping("/search/{searchString}")
    ResponseEntity<ResourceList<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    /**
     * @param id Employee id
     * @return Employee with specified id. 
     */
    @GetMapping("/{id}")
    ResponseEntity<ResourceData<Employee>> getEmployeeById(@PathVariable Integer id);

    /**
     * @return a single integer indicating the highest salary of all employees.
     */
    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    /**
     * @return a list of the top 10 employees based off of their salaries
     */
    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    /**
     * @param employeeInput Employee details request payload
     * @return a status of success or failed based on if an employee was created
     */
    @PostMapping(consumes="application/json")
    ResponseEntity<DummyApiOperationResponse> createEmployee(@RequestBody Map<String, Object> employeeInput);

    /**
     * @param id delete employee with specified id.
     * @return id of the employee that was deleted.
     */
    @DeleteMapping("/{id}")
    ResponseEntity<DummyApiDeleteResponse> deleteEmployeeById(@PathVariable String id);

}
