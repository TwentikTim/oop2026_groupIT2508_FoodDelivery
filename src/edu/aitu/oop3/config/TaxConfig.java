package edu.aitu.oop3.config;

public class TaxConfig {


    private static TaxConfig instance;


    private final double taxRate;


    private TaxConfig() {
        this.taxRate = 0.12;
    }

    public static TaxConfig getInstance() {
        if (instance == null) {
            instance = new TaxConfig();
        }
        return instance;
    }

    public double getTaxRate() {
        return taxRate;
    }
}

