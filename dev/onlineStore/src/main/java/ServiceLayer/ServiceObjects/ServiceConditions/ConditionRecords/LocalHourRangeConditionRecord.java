package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;

public record LocalHourRangeConditionRecord(int lowerBoundaryHour, int lowerBoundaryMin, int upperBoundaryHour, int upperBoundaryMin) implements ConditionRecord {

    @Override
    public Discount acceptDiscount(ConditionFactory factory, String description, double amount) {
        return factory.addDiscount(this,description,amount);
    }
    @Override
    public Policy acceptPolicy(ConditionFactory factory, String description) {
        return factory.addPolicy(this,description);
    }
}