package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.ConditionFactory;
import DomainLayer.Stores.Discounts.Discount;

/**
 * if a condition passes, runs a second condition on all the products that pass the first condition, in a new separate bag with its own price
 */
public record CheckForConditionRecord(int id1, int id2) implements ConditionRecord {
    @Override
    public Discount accept(ConditionFactory factory, String description, double amount) {
        return factory.addDiscount(this,description,amount);
    }
}
