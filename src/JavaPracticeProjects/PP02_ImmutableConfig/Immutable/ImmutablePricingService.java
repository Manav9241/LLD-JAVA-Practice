package JavaPracticeProjects.PP02_ImmutableConfig.Immutable;

public class ImmutablePricingService {
    private final ImmutableConfig config;

    public ImmutablePricingService(ImmutableConfig config) {
        this.config = config;
    }

    public double CalculatePrice(double basePrice) {
        double tax = Double.parseDouble(config.get("tax"));
        return basePrice + (basePrice * tax);
    }
}
