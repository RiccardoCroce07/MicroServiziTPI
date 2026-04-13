package com.example.salary_analytics;

import com.example.salary_analytics.SalaryRequest;
import com.example.salary_analytics.SalaryResponse;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    /**
     * Stima la RAL lorda in base all'età usando una curva realistica
     * del mercato IT italiano (settore tech/impiegatizio).
     *
     * Fascia età → RAL stimata:
     *   < 25  → Junior  → 22.000
     *   25-30 → Junior+ → 27.000
     *   31-37 → Mid     → 35.000
     *   38-45 → Senior  → 46.000
     *   > 45  → Lead    → 58.000
     *
     * Viene aggiunto un "jitter deterministico" basato sulla somma
     * dei codici ASCII del cognome, così ogni impiegato ha una RAL
     * leggermente diversa (±15%) ma sempre riproducibile.
     */
    private double stimaRal(int age, String cognome) {
        double base;
        if      (age < 25) base = 22_000;
        else if (age < 31) base = 27_000;
        else if (age < 38) base = 35_000;
        else if (age < 46) base = 46_000;
        else               base = 58_000;

        // Jitter deterministico ±15% basato sul cognome
        int hash = 0;
        if (cognome != null) {
            for (char c : cognome.toCharArray()) hash += c;
        }
        double variazione = ((hash % 31) - 15) / 100.0; // da -0.15 a +0.15
        return Math.round(base * (1 + variazione) / 100.0) * 100.0; // arrotonda a 100
    }

    private String categoriaRuolo(int age) {
        if (age < 25) return "Junior";
        if (age < 31) return "Junior+";
        if (age < 38) return "Mid-Level";
        if (age < 46) return "Senior";
        return "Lead / Principal";
    }

    /**
     * Calcolo IRPEF 2024 con scaglioni reali:
     *   0     – 28.000 → 23%
     *   28.001 – 50.000 → 35%
     *   > 50.000        → 43%
     */
    private double[] calcolaIrpef(double ral) {
        double s1 = 0, s2 = 0, s3 = 0;

        if (ral <= 28_000) {
            s1 = ral * 0.23;
        } else if (ral <= 50_000) {
            s1 = 28_000 * 0.23;
            s2 = (ral - 28_000) * 0.35;
        } else {
            s1 = 28_000 * 0.23;
            s2 = (50_000 - 28_000) * 0.35;
            s3 = (ral - 50_000) * 0.43;
        }

        return new double[]{ round2(s1), round2(s2), round2(s3) };
    }

    /**
     * Detrazione per lavoro dipendente (art. 13 TUIR):
     *   RAL ≤ 15.000 → 1.955 (+ quota proporzionale)
     *   15.001–28.000 → 1.910 − progressiva
     *   28.001–50.000 → 1.910 − progressiva
     *   > 50.000      → 0
     */
    private double calcolaDetrazione(double ral) {
        if (ral <= 15_000) {
            double q = 690 + 1_380 * ((15_000 - ral) / 15_000);
            return round2(Math.min(1_955 + q, 3_000));
        } else if (ral <= 28_000) {
            return round2(1_910 * ((28_000 - ral) / 13_000) + 978);
        } else if (ral <= 50_000) {
            return round2(1_910 * ((50_000 - ral) / 22_000));
        }
        return 0;
    }

    /**
     * Contributi INPS lavoratore dipendente: 9,19% fino al massimale
     * (pensione IVS: 9,19%; disoccupazione inclusa)
     */
    private double calcolaInps(double ral) {
        double aliquota = 0.0919;
        double massimale = 119_650; // massimale 2024 (approssimato)
        double imponibile = Math.min(ral, massimale);
        return round2(imponibile * aliquota);
    }

    /**
     * Salary Score 0–100 rispetto alla RAL media italiana
     * (media ISTAT ~29.000 €/anno al 2024).
     * Score 50 = media. Curva logaritmica per non penalizzare troppo i bassi.
     */
    private int calcolaSalaryScore(double ral) {
        double media = 29_000;
        double ratio = ral / media;
        // logistica centrata su ratio=1 → score=50
        double score = 100 / (1 + Math.exp(-3.5 * (ratio - 1)));
        return (int) Math.round(Math.min(100, Math.max(0, score)));
    }

    private String salaryBenchmark(int score) {
        if (score < 40) return "Sotto la media nazionale";
        if (score < 60) return "In linea con la media nazionale";
        if (score < 80) return "Sopra la media nazionale";
        return "Top 20% nazionale";
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // ---- ENTRY POINT ----

    public SalaryResponse calcola(SalaryRequest req) {

        // Usa il salary dal DB invece di stimarlo
        double ral = req.getSalary();

        // Se per qualche motivo è 0, fallback alla stima per età
        if (ral <= 0) {
            ral = stimaRal(req.getAge(), req.getCognome());
        }

            // tutto il resto del calcolo rimane identico...

        double[] irpef = calcolaIrpef(ral);
        double irpefTotale = irpef[0] + irpef[1] + irpef[2];
        double detrazione  = calcolaDetrazione(ral);
        double irpefNetta  = Math.max(0, irpefTotale - detrazione);

        double inpsAnnuo   = calcolaInps(ral);
        double nettoAnnuo  = round2(ral - irpefNetta - inpsAnnuo);

        int score = calcolaSalaryScore(ral);

        SalaryResponse r = new SalaryResponse();
        r.setEmployeeName(req.getName() + " " + req.getCognome());
        r.setAge(req.getAge());
        r.setRoleCategory(categoriaRuolo(req.getAge()));

        r.setRalLorda(ral);
        r.setMensileLordo(round2(ral / 13.0));  // 13 mensilità

        r.setIrpefScaglione1(irpef[0]);
        r.setIrpefScaglione2(irpef[1]);
        r.setIrpefScaglione3(irpef[2]);
        r.setIrpefTotaleAnnuo(round2(irpefNetta));
        r.setIrpefMensile(round2(irpefNetta / 13.0));

        r.setDetrazioniLavoroDipendente(detrazione);

        r.setInpsAnnuo(inpsAnnuo);
        r.setInpsMensile(round2(inpsAnnuo / 13.0));

        r.setNettoAnnuo(nettoAnnuo);
        r.setNettoMensile(round2(nettoAnnuo / 13.0));

        r.setAliquotaEffettiva(round2((irpefNetta + inpsAnnuo) / ral * 100));
        r.setSalaryScore(score);
        r.setSalaryBenchmark(salaryBenchmark(score));

        return r;
    }
}