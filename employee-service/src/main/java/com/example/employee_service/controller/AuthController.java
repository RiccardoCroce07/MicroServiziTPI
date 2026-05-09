package com.example.employee_service.controller;

import com.example.employee_service.entity.User;
import com.example.employee_service.repository.UserRepository;
import com.example.employee_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtService jwtService;

    // POST /auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String,String> body) {
        if (userRepo.findByUsername(body.get("username")).isPresent())
            return ResponseEntity.badRequest().body("Username già esistente");

        User user = new User();
        user.setUsername(body.get("username"));
        user.setPassword(encoder.encode(body.get("password")));
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utente registrato!");
    }

    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> body) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.get("username"), body.get("password")));

        UserDetails user = userRepo.findByUsername(body.get("username")).orElseThrow();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}