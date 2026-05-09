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
        // Validazione età
        if (request.getAge() <= 0)
            throw new IllegalArgumentException("L'età deve essere maggiore di 0");

        // Validazione stipendio
        if (request.getSalary() == null || request.getSalary() <= 0)
            throw new IllegalArgumentException("Lo stipendio deve essere maggiore di 0");

        // Controllo nome + cognome duplicato
        if (employeeRepo.existsByNameAndCognome(request.getName(), request.getCognome()))
            throw new IllegalArgumentException("Esiste già un impiegato con nome '" + request.getName() + "' e cognome '" + request.getCognome() + "'");

        // Controllo email unica
        if (request.getEmail() != null && employeeRepo.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("L'email '" + request.getEmail() + "' è già registrata");

        Employee employee = mapper.map(request, Employee.class);
        Employee saved = employeeRepo.save(employee);
        return mapper.map(saved, EmployeeResponse.class);
    }

    // FASE 2 - PUT: sostituzione completa impiegato
    public EmployeeResponse replaceEmployee(int id, EmployeeRequest request) {
        if (!employeeRepo.existsById(id))
            throw new RuntimeException("Impiegato con id " + id + " non trovato");

        // Validazione età
        if (request.getAge() <= 0)
            throw new IllegalArgumentException("L'età deve essere maggiore di 0");

        // Validazione stipendio
        if (request.getSalary() == null || request.getSalary() <= 0)
            throw new IllegalArgumentException("Lo stipendio deve essere maggiore di 0");

        // Controllo nome + cognome duplicato (escludo l'impiegato stesso)
        if (employeeRepo.existsByNameAndCognomeAndIdNot(request.getName(), request.getCognome(), id))
            throw new IllegalArgumentException("Esiste già un altro impiegato con nome '" + request.getName() + "' e cognome '" + request.getCognome() + "'");

        // Controllo email unica (escludo l'impiegato stesso)
        if (request.getEmail() != null && employeeRepo.existsByEmailAndIdNot(request.getEmail(), id))
            throw new IllegalArgumentException("L'email '" + request.getEmail() + "' è già registrata da un altro impiegato");

        Employee employee = mapper.map(request, Employee.class);
        employee.setId(id);
        Employee saved = employeeRepo.save(employee);
        return mapper.map(saved, EmployeeResponse.class);
    }

    // FASE 2 - PATCH: aggiornamento parziale impiegato
    public EmployeeResponse updateEmployee(int id, EmployeeRequest request) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Impiegato con id " + id + " non trovato"));

        // Validazione età se fornita
        if (request.getAge() > 0) {
            employee.setAge(request.getAge());
        } else if (request.getAge() < 0) {
            throw new IllegalArgumentException("L'età non può essere negativa");
        }

        // Validazione stipendio se fornito
        if (request.getSalary() != null) {
            if (request.getSalary() <= 0)
                throw new IllegalArgumentException("Lo stipendio deve essere maggiore di 0");
            employee.setSalary(request.getSalary());
        }

        if (request.getName() != null)    employee.setName(request.getName());
        if (request.getCognome() != null) employee.setCognome(request.getCognome());

        // Controllo nome + cognome duplicato (escludo l'impiegato stesso)
        if (employeeRepo.existsByNameAndCognomeAndIdNot(employee.getName(), employee.getCognome(), id))
            throw new IllegalArgumentException("Esiste già un altro impiegato con nome '" + employee.getName() + "' e cognome '" + employee.getCognome() + "'");

        if (request.getEmail() != null) {
            // Controllo email unica (escludo l'impiegato stesso)
            if (employeeRepo.existsByEmailAndIdNot(request.getEmail(), id))
                throw new IllegalArgumentException("L'email '" + request.getEmail() + "' è già registrata da un altro impiegato");
            employee.setEmail(request.getEmail());
        }

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
