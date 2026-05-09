package com.example.employee_service.config;

import com.example.employee_service.entity.Employee;
import com.example.employee_service.entity.User;
import com.example.employee_service.repository.EmployeeRepo;
import com.example.employee_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * DataLoader — attivo SOLO con profilo "docker" (autonomo).
 * Inserisce dipendenti e utenti di esempio solo se le tabelle sono vuote.
 */
@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(EmployeeRepo empRepo,
                                      UserRepository userRepo,
                                      PasswordEncoder encoder) {
        return args -> {

            // ── Dipendenti ──────────────────────────────────────
            if (empRepo.count() == 0) {
                System.out.println("[DataLoader] Inserimento dipendenti...");
                empRepo.saveAll(List.of(
                    creaEmp("Mario",      "Rossi",    "mario.rossi@mail.it",    35, 38000.0),
                    creaEmp("Laura",      "Bianchi",  "laura.bianchi@mail.it",  28, 27000.0),
                    creaEmp("Giovanni",   "Verdi",    "giovanni.verdi@mail.it", 42, 46000.0),
                    creaEmp("Sofia",      "Ferrari",  "sofia.ferrari@mail.it",  31, 35000.0),
                    creaEmp("Alessandro", "Conti",    "ale.conti@mail.it",      24, 22500.0),
                    creaEmp("Chiara",     "Russo",    "chiara.russo@mail.it",   38, 41000.0),
                    creaEmp("Luca",       "Esposito", "luca.esposito@mail.it",  27, 26000.0),
                    creaEmp("Martina",    "Romano",   "martina.romano@mail.it", 45, 52000.0)
                ));
                System.out.println("[DataLoader] 8 dipendenti inseriti.");
            } else {
                System.out.println("[DataLoader] Dipendenti già presenti, skip.");
            }

            // ── Utenti ───────────────────────────────────────────
            if (userRepo.count() == 0) {
                System.out.println("[DataLoader] Inserimento utenti...");
                userRepo.saveAll(List.of(
                    creaUser("croceadmin", "beeswarm", "ADMIN", encoder),
                    creaUser("user1",      "pass1",    "USER",  encoder),
                    creaUser("user2",      "pass2",    "USER",  encoder),
                    creaUser("user3",      "pass3",    "USER",  encoder)
                ));
                System.out.println("[DataLoader] 4 utenti inseriti (1 admin + 3 user).");
            } else {
                System.out.println("[DataLoader] Utenti già presenti, skip.");
            }
        };
    }

    private Employee creaEmp(String name, String cognome, String email, int age, double salary) {
        Employee e = new Employee();
        e.setName(name);
        e.setCognome(cognome);
        e.setEmail(email);
        e.setAge(age);
        e.setSalary(salary);
        return e;
    }

    private User creaUser(String username, String password, String ruolo, PasswordEncoder enc) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(enc.encode(password));
        u.setRuolo(ruolo);
        return u;
    }
}
