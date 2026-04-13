package com.example.salary_analytics;

public class SalaryRequest {

    private String name;
    private String cognome;
    private int age;
    private String email;
    private double salary;

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
