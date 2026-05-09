package com.example.employee_service.repository;

import com.example.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    List<Employee> findByCognome(String cognome);

}
