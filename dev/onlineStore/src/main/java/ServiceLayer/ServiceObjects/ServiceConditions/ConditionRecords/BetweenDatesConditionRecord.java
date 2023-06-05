package ServiceLayer.ServiceObjects.ServiceConditions.ConditionRecords;

import DomainLayer.Stores.Discounts.Discount;
import DomainLayer.Stores.Store;

public record BetweenDatesConditionRecord(int fromDay, int fromMonth, int fromYear, int untilDay, int untilMonth, int untilYear) implements ConditionRecord {

    @Override
    public Discount accept(Store store, String description, double amount) {
        return store.addDiscount(this,description,amount);
    }
}
