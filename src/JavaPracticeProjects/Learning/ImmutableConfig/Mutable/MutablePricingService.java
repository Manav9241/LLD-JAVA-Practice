package JavaPracticeProjects.Learning.ImmutableConfig.Mutable;

public class MutablePricingService {
    private final MutableConfig config;

    public MutablePricingService(MutableConfig config) {
        this.config = config;
    }

    public double CalculatePrice(double basePrice) {
        double tax = Double.parseDouble(config.get("tax"));
        return basePrice + (basePrice * tax);
    }
}
