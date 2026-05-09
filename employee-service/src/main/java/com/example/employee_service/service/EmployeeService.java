package com.example.employee_service.service;

import com.example.employee_service.entity.Employee;
import com.example.employee_service.repository.EmployeeRepo;
import com.example.employee_service.dto.EmployeeRequest;
import com.example.employee_service.dto.EmployeeResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper mapper;

    // FASE 1a - Ricerca per ID
    public EmployeeResponse getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepo.findById(id);
        if (employee.isEmpty()) {
            throw new RuntimeException("Impiegato con id " + id + " non trovato");
        }
        return mapper.map(employee.get(), EmployeeResponse.class);
    }

    // FASE 1b - Elenco completo
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(emp -> mapper.map(emp, EmployeeResponse.class))
                .collect(Collectors.toList());
    }

    // FASE 1c - Ricerca per cognome
    public List<EmployeeResponse> getEmployeesByCognome(String cognome) {
        List<Employee> employees = employeeRepo.findByCognome(cognome);
        return employees.stream()
                .map(emp -> mapper.map(emp, EmployeeResponse.class))
                .collect(Collectors.toList());
    }

    // FASE 2 - POST: crea nuovo impiegato
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = mapper.map(request, Employee.class);
        Employee saved = employeeRepo.save(employee);
        return mapper.map(saved, EmployeeResponse.class);
    }

    // FASE 2 - PUT: sostituzione completa impiegato
    public EmployeeResponse replaceEmployee(int id, EmployeeRequest request) {
        if (!employeeRepo.existsById(id)) {
            throw new RuntimeException("Impiegato con id " + id + " non trovato");
        }
        Employee employee = mapper.map(request, Employee.class);
        employee.setId(id);
        Employee saved = employeeRepo.save(employee);
        return mapper.map(saved, EmployeeResponse.class);
    }

    // FASE 2 - PATCH: aggiornamento parziale impiegato
    public EmployeeResponse updateEmployee(int id, EmployeeRequest request) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Impiegato con id " + id + " non trovato"));

        if (request.getName() != null)    employee.setName(request.getName());
        if (request.getCognome() != null) employee.setCognome(request.getCognome());
        if (request.getEmail() != null)   employee.setEmail(request.getEmail());
        if (request.getAge() > 0)         employee.setAge(request.getAge());
        if (request.getSalary() != null)  employee.setSalary(request.getSalary());

        Employee saved = employeeRepo.save(employee);
        return mapper.map(saved, EmployeeResponse.class);
    }

    // FASE 2 - DELETE: elimina impiegato
    public String deleteEmployee(int id) {
        if (!employeeRepo.existsById(id)) {
            throw new RuntimeException("Impiegato con id " + id + " non trovato");
        }
        employeeRepo.deleteById(id);
        return "Impiegato con id " + id + " eliminato con successo";
    }
}
