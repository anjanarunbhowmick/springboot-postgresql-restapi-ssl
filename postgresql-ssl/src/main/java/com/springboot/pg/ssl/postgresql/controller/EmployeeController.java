package com.springboot.pg.ssl.postgresql.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.pg.ssl.postgresql.helper.EmployeeUtils;
import com.springboot.pg.ssl.postgresql.model.Employee;
import com.springboot.pg.ssl.postgresql.model.PhoneNumbers;
import com.springboot.pg.ssl.postgresql.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/employees")
	public ResponseEntity<Map<String, Object>> getAllEmployees(){
		List<Employee> employees = employeeRepository.findAll();
		Map<String, Object> response = new HashMap<>();
		response.put("records", employees);
		response.put("totalRecords", employees != null ? employees.size() : 0);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Employee employee){
		employee.setPhoneNumbers(EmployeeUtils.deduplicateByField(employee.getPhoneNumbers(), PhoneNumbers::getPhoneNumber));
		employeeRepository.save(employee);
		Map<String, Object> response = new HashMap<>();
		response.put("records", employee);
		response.put("totalRecords", employee != null ? 1 : 0);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable(name="id") UUID employeeId) {
		Employee employee = null;
		Map<String, Object> response = new HashMap<>();
		try {
			employee = employeeRepository.findById(employeeId).orElseThrow(
					() -> new Exception("Employee ID :: "+employeeId+" does not exist"));
			response.put("records", employee);
		} catch (Exception e) {
			response.put("error", e.getMessage());
		}
	
		response.put("totalRecords", employee != null ? 1 : 0);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable(name="id") UUID employeeId, @RequestBody Employee tempEmployee) {
		Employee employee = null;
		Map<String, Object> response = new HashMap<>();
		try {
			employee = employeeRepository.findById(employeeId).orElseThrow(
					() -> new Exception("Employee ID :: "+employeeId+" does not exist"));
			employee.setFirstName(tempEmployee.getFirstName());
			employee.setLastName(tempEmployee.getLastName());
			employee.setEmailId(tempEmployee.getEmailId());
			employeeRepository.save(employee);
			response.put("records", employee);
		} catch (Exception e) {
			response.put("error", e.getMessage());
		}
		
		response.put("totalRecords", employee != null ? 1 : 0);
		return ResponseEntity.ok(response);
	}
	 
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable(name="id") UUID employeeId) {
		
		Employee employee = null;
		Map<String, Object> response = new HashMap<>();
		try {
			employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new Exception("Employee ID :: "+employeeId+" does not exist"));
			employeeRepository.delete(employee);
			response.put("id", employeeId);
			response.put("isDeleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put("error", e.getMessage());
			response.put("isDeleted", Boolean.FALSE);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/employees/phone")
	public ResponseEntity<Map<String, Object>> createPhone(@RequestHeader("employeeid") UUID employeeId, @RequestBody PhoneNumbers phoneNumbers) {
		Map<String, Object> response = new HashMap<>();
		try {

			Employee employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new Exception("Employee ID :: " + employeeId + " does not exist"));
			Set<PhoneNumbers> phNumbers = employee.getPhoneNumbers();
			if (phNumbers != null) {
				phNumbers.add(phoneNumbers);
			} else {
				phNumbers = new HashSet<PhoneNumbers>();
				phNumbers.add(phoneNumbers);
			}
			employee.setPhoneNumbers(EmployeeUtils.deduplicateByField(phNumbers, PhoneNumbers::getPhoneNumber));
			employeeRepository.save(employee);
			response.put("records", employee);

		} catch (Exception e) {
			response.put("error", e.getMessage());
			response.put("totalRecords", 0);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/employees/phone/{id}")
	public ResponseEntity<Map<String, Object>> updatePhone(@PathVariable(name="id") UUID phoneId, @RequestHeader("employeeid") UUID employeeId, @RequestBody PhoneNumbers phoneNumbers) {
		Map<String, Object> response = new HashMap<>();
		try {
			Employee employee = employeeRepository.findById(employeeId).orElseThrow(
					() -> new Exception("Employee ID :: "+employeeId+" does not exist"));
			Set<PhoneNumbers> phNumbers = employee.getPhoneNumbers();
			for(PhoneNumbers ph : phNumbers) {
				if(ph.getId().equals(phoneId)) {
					ph.setPhoneNumber(phoneNumbers.getPhoneNumber());
					response.put("totalRecords", 1);
				}
			}
			employee.setPhoneNumbers(EmployeeUtils.deduplicateByField(phNumbers, PhoneNumbers::getPhoneNumber));
			employeeRepository.save(employee);
			response.put("records", employee);
		} catch (Exception e) {
			response.put("error", e.getMessage());
			response.put("totalRecords", 0);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/employees/phone/{id}")
	public ResponseEntity<Map<String, Object>> deletePhone(@PathVariable(name="id") UUID phoneId, @RequestHeader("employeeid") UUID employeeId) {
		
		Employee employee = null;
		Map<String, Object> response = new HashMap<>();
		try {
			employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new Exception("Employee ID :: "+employeeId+" does not exist"));
			Set<PhoneNumbers> phNumbers = employee.getPhoneNumbers();
			for(Iterator<PhoneNumbers> it = phNumbers.iterator(); it.hasNext();) {
				PhoneNumbers ph = it.next();
				if(ph.getId().equals(phoneId)) {
					it.remove();
					response.put("totalRecords", 1);
				}
			}
			employee.setPhoneNumbers(phNumbers);
			employeeRepository.save(employee);
			response.put("id", employeeId);
			response.put("isDeleted", Boolean.TRUE);
		} catch (Exception e) {
			response.put("error", e.getMessage());
			response.put("isDeleted", Boolean.FALSE);
		}
		
		return ResponseEntity.ok(response);
	}
}
