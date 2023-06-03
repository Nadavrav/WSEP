package ServiceLayer.ServiceObjects.ServiceDiscounts;


import DomainLayer.Stores.Conditions.ConditionTypes.Condition;

import java.util.Set;

public class ServiceDiscount {
    private final String discountDescription;

    public ServiceDiscount(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }
}
