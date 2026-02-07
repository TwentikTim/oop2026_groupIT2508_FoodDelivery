package edu.aitu.oop3.BillingComponent;

public class TaxConfig {

    private static volatile TaxConfig instance;

    private final double TaxRate = 0.12;

    private TaxConfig() {
        System.out.println("TaxConfig instance created");
    }

    public static TaxConfig getInstance() {
        if (instance == null) {
            synchronized (TaxConfig.class) {
                if (instance == null) {
                    instance = new TaxConfig();
                }
            }
        }
        return instance;
    }

    public double getTaxRate() {
        return TaxRate;
    }
}
