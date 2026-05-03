package com.example.salary_analytics;

public class SalaryResponse {

    private String employeeName;
    private int age;
    private String roleCategory;       // Junior / Mid / Senior / Lead

    private double ralLorda;           // Reddito Annuo Lordo stimato
    private double mensileLordo;       // RAL / 13

    // IRPEF breakdown (scaglioni 2024)
    private double irpefScaglione1;    // 23% su primi 28.000
    private double irpefScaglione2;    // 35% su 28.001-50.000
    private double irpefScaglione3;    // 43% su oltre 50.000
    private double irpefTotaleAnnuo;
    private double irpefMensile;

    // No Tax Area / Detrazioni lavoro dipendente
    private double detrazioniLavoroDipendente;

    // INPS (contributi lavoratore: 9,19%)
    private double inpsAnnuo;
    private double inpsMensile;

    // Netto
    private double nettoAnnuo;
    private double nettoMensile;

    // Extra
    private double aliquotaEffettiva;  // % tasse reali sul lordo
    private int    salaryScore;        // 0-100 rispetto alla media italiana
    private String salaryBenchmark;    // "Sotto media" / "In linea" / "Sopra media"

    // --- Getters & Setters ---

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getRoleCategory() { return roleCategory; }
    public void setRoleCategory(String roleCategory) { this.roleCategory = roleCategory; }

    public double getRalLorda() { return ralLorda; }
    public void setRalLorda(double ralLorda) { this.ralLorda = ralLorda; }

    public double getMensileLordo() { return mensileLordo; }
    public void setMensileLordo(double mensileLordo) { this.mensileLordo = mensileLordo; }

    public double getIrpefScaglione1() { return irpefScaglione1; }
    public void setIrpefScaglione1(double irpefScaglione1) { this.irpefScaglione1 = irpefScaglione1; }

    public double getIrpefScaglione2() { return irpefScaglione2; }
    public void setIrpefScaglione2(double irpefScaglione2) { this.irpefScaglione2 = irpefScaglione2; }

    public double getIrpefScaglione3() { return irpefScaglione3; }
    public void setIrpefScaglione3(double irpefScaglione3) { this.irpefScaglione3 = irpefScaglione3; }

    public double getIrpefTotaleAnnuo() { return irpefTotaleAnnuo; }
    public void setIrpefTotaleAnnuo(double irpefTotaleAnnuo) { this.irpefTotaleAnnuo = irpefTotaleAnnuo; }

    public double getIrpefMensile() { return irpefMensile; }
    public void setIrpefMensile(double irpefMensile) { this.irpefMensile = irpefMensile; }

    public double getDetrazioniLavoroDipendente() { return detrazioniLavoroDipendente; }
    public void setDetrazioniLavoroDipendente(double detrazioniLavoroDipendente) { this.detrazioniLavoroDipendente = detrazioniLavoroDipendente; }

    public double getInpsAnnuo() { return inpsAnnuo; }
    public void setInpsAnnuo(double inpsAnnuo) { this.inpsAnnuo = inpsAnnuo; }

    public double getInpsMensile() { return inpsMensile; }
    public void setInpsMensile(double inpsMensile) { this.inpsMensile = inpsMensile; }

    public double getNettoAnnuo() { return nettoAnnuo; }
    public void setNettoAnnuo(double nettoAnnuo) { this.nettoAnnuo = nettoAnnuo; }

    public double getNettoMensile() { return nettoMensile; }
    public void setNettoMensile(double nettoMensile) { this.nettoMensile = nettoMensile; }

    public double getAliquotaEffettiva() { return aliquotaEffettiva; }
    public void setAliquotaEffettiva(double aliquotaEffettiva) { this.aliquotaEffettiva = aliquotaEffettiva; }

    public int getSalaryScore() { return salaryScore; }
    public void setSalaryScore(int salaryScore) { this.salaryScore = salaryScore; }

    public String getSalaryBenchmark() { return salaryBenchmark; }
    public void setSalaryBenchmark(String salaryBenchmark) { this.salaryBenchmark = salaryBenchmark; }
}
