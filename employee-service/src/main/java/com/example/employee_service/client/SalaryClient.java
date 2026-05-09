package com.example.employee_service.client;

import com.example.employee_service.dto.SalaryResponse;
import com.example.employee_service.entity.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Client RestClient che chiama MS2 (SalaryAnalyticsService) sulla porta 8081.
 * MS1 recupera l'impiegato dal DB, poi delega il calcolo stipendiale a MS2.
 */
@Component
public class SalaryClient {

    private final RestClient restClient;

    public SalaryClient(@Value("${salary.service.url:http://localhost:8081}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Invia i dati dell'impiegato a MS2 e riceve la scheda stipendiale completa.
     */
    public SalaryResponse getSalaryAnalysis(Employee employee) {
        Map<String, Object> body = Map.of(
                "name",    employee.getName(),
                "cognome", employee.getCognome(),
                "age",     employee.getAge(),
                "email",   employee.getEmail() != null ? employee.getEmail() : "",
                "salary",  employee.getSalary() != null ? employee.getSalary() : 0.0
        );
        // resto invariato

        return restClient.post()
                .uri("/salary/calcola")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(SalaryResponse.class);
    }
}
