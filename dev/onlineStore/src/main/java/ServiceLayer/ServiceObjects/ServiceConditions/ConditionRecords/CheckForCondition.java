package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Store;
/**
 * if a condition passes, runs a second condition on all the products that pass the first condition, in a new separate bag with its own price
 */
public record CheckForCondition(int id1,int id2) implements ConditionRecord {
    @Override
    public Discount accept(Store store, String description, double amount) {
        return store.addDiscount(this,description,amount);
    }
}
