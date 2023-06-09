package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;
import ServiceLayer.ServiceObjects.ServiceDiscounts.ServiceBasicDiscount;

public record NameConditionRecord(String name) implements ConditionRecord {
    @Override
    public Discount acceptDiscount(ConditionFactory factory, String description, double amount) {
        return factory.addDiscount(this,description,amount);
    }
    @Override
    public Policy acceptPolicy(ConditionFactory factory, String description) {
        NameConditionRecord nameConditionRecord=new NameConditionRecord("Milk");
        ServiceBasicDiscount serviceBasicDiscount=new ServiceBasicDiscount("50% disocunt on milk",50,new NameConditionRecord("Milk"));
        return factory.addPolicy(this,description);
    }
}
