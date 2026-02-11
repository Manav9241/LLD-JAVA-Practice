package P02_TomatoFoodDeliveryApp.V02_BetterDesign.factory;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.PaymentMethod;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.CardPaymentStrategy;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.PaymentStrategy;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.UPIPaymentStrategy;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.WalletPaymentStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PaymentStrategyFactory {
    private final Map<PaymentMethod, Function<String, PaymentStrategy>> strategyCreators;

    public PaymentStrategyFactory() {
        strategyCreators = new HashMap<>();
        strategyCreators.put(PaymentMethod.UPI, UPIPaymentStrategy::new);
        strategyCreators.put(PaymentMethod.CARD, CardPaymentStrategy::new);
        strategyCreators.put(PaymentMethod.WALLET, WalletPaymentStrategy::new);
    }

    public PaymentStrategy createPaymentStrategy(PaymentMethod method, String paymentDetails) {
        Function<String, PaymentStrategy> creator = strategyCreators.get(method);
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
        return creator.apply(paymentDetails);
    }

    public void registerPaymentMethod(PaymentMethod method, Function<String, PaymentStrategy> creator) {
        strategyCreators.put(method, creator);
    }
}
