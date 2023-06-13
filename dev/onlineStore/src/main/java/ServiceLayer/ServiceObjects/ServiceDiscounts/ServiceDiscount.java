package ServiceLayer.ServiceObjects.ServiceDiscounts;


import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;

public abstract class ServiceDiscount {
    public final String description;


    public ServiceDiscount(String description){
        this.description=description;
    }

    public ServiceDiscount(Discount discount) {
        description=discount.getDescription();
    }

    public abstract Discount accept(ConditionFactory conditionFactory);
}
