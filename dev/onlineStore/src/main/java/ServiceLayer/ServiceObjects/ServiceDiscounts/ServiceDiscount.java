package ServiceLayer.ServiceObjects.ServiceDiscounts;

import DomainLayer.Stores.Conditions.Condition;

import java.util.Set;

public class ServiceDiscount {
    private final Set<Condition> discountConditions;
    private final double discountAmount;
    private final String discountDescription;

    public ServiceDiscount(Set<Condition> discountConditions, double discountAmount, String discountDescription) {
        this.discountConditions = discountConditions;
        this.discountAmount = discountAmount;
        this.discountDescription = discountDescription;
    }
}
