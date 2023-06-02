package ServiceLayer.ServiceObjects.ServiceDiscounts;


import DomainLayer.Stores.Conditions.ConditionTypes.Condition;

import java.util.Set;

public class ServiceDiscount {
    private final String discountDescription;
    private final int id;

    public ServiceDiscount(String discountDescription, int id) {
        this.discountDescription = discountDescription;
        this.id=id;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }
}
