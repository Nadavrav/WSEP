package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Conditions.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Policies.Policy;

/**
 * if a condition passes, runs a second condition on all the products that pass the first condition, in a new separate bag with its own price
 */
public record CheckForConditionRecord(int id1, int id2) implements ConditionRecord {
    @Override
    public Discount acceptDiscount(ConditionFactory factory, String description, double amount) {
        return factory.addDiscount(this,description,amount);
    }
    @Override
    public Policy acceptPolicy(ConditionFactory factory, String description) {
        return factory.addPolicy(this,description);
    }
}
