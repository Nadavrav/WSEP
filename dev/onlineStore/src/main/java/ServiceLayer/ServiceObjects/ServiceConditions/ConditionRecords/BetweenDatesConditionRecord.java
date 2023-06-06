package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;

public record BetweenDatesConditionRecord(int fromDay, int fromMonth, int fromYear, int untilDay, int untilMonth, int untilYear) implements ConditionRecord {

    @Override
    public Discount acceptDiscount(ConditionFactory factory, String description, double amount) {
        return factory.addDiscount(this,description,amount);
    }
    @Override
    public Policy acceptPolicy(ConditionFactory factory, String description) {
        return factory.addPolicy(this,description);
    }
}
