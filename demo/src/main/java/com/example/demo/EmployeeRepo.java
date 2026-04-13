package com.example.demo;

import com.example.demo.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    List<Employee> findByCognome(String cognome);

}
